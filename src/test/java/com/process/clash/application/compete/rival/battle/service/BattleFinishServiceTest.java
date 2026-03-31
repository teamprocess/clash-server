package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.rival.battle.enums.BattleStatus;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.rival.rival.enums.RivalLinkingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BattleFinishServiceTest {

    @Mock
    private BattleRepositoryPort battleRepositoryPort;

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    @Mock
    private UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;

    private BattleFinishService battleFinishService;

    private static final Long BATTLE_ID = 1L;
    private static final Long RIVAL_ID = 10L;
    private static final Long FIRST_USER_ID = 100L;
    private static final Long SECOND_USER_ID = 200L;

    @BeforeEach
    void setUp() {
        battleFinishService = new BattleFinishService(battleRepositoryPort, rivalRepositoryPort, userExpHistoryRepositoryPort);
    }

    @Test
    @DisplayName("종료일이 지난 IN_PROGRESS 배틀을 DONE으로 전환한다")
    void finishExpiredBattles_transitionsInProgressToDone() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(7);
        LocalDate endDate = today.minusDays(1);
        Battle inProgressBattle = new Battle(BATTLE_ID, Instant.now(), Instant.now(),
                startDate, endDate, BattleStatus.IN_PROGRESS, null, RIVAL_ID, 20L);

        when(battleRepositoryPort.findExpiredInProgressBattles(today)).thenReturn(List.of(inProgressBattle));
        when(battleRepositoryPort.findExpiredNotStartedBattles(today)).thenReturn(List.of());
        stubRivalAndExp(inProgressBattle, 50.0, 30.0);

        battleFinishService.finishExpiredBattles(today);

        ArgumentCaptor<List<Battle>> captor = ArgumentCaptor.forClass(List.class);
        verify(battleRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue()).allMatch(b -> b.battleStatus() == BattleStatus.DONE);
    }

    @Test
    @DisplayName("종료일이 지난 NOT_STARTED 배틀을 CANCELED로 전환한다")
    void finishExpiredBattles_transitionsNotStartedToCanceled() {
        LocalDate today = LocalDate.now();
        Battle notStartedBattle = new Battle(2L, Instant.now(), Instant.now(),
                today.minusDays(7), today.minusDays(1), BattleStatus.NOT_STARTED, null, RIVAL_ID, 20L);

        when(battleRepositoryPort.findExpiredInProgressBattles(today)).thenReturn(List.of());
        when(battleRepositoryPort.findExpiredNotStartedBattles(today)).thenReturn(List.of(notStartedBattle));

        battleFinishService.finishExpiredBattles(today);

        ArgumentCaptor<List<Battle>> captor = ArgumentCaptor.forClass(List.class);
        verify(battleRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue()).allMatch(b -> b.battleStatus() == BattleStatus.CANCELED);
    }

    @Test
    @DisplayName("IN_PROGRESS와 NOT_STARTED 만료 배틀을 한 번의 saveAll로 처리한다")
    void finishExpiredBattles_savesAllInSingleCall() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(7);
        LocalDate endDate = today.minusDays(1);
        Battle inProgressBattle = new Battle(BATTLE_ID, Instant.now(), Instant.now(),
                startDate, endDate, BattleStatus.IN_PROGRESS, null, RIVAL_ID, 20L);
        Battle notStartedBattle = new Battle(2L, Instant.now(), Instant.now(),
                startDate, endDate, BattleStatus.NOT_STARTED, null, RIVAL_ID, 20L);

        when(battleRepositoryPort.findExpiredInProgressBattles(today)).thenReturn(List.of(inProgressBattle));
        when(battleRepositoryPort.findExpiredNotStartedBattles(today)).thenReturn(List.of(notStartedBattle));
        stubRivalAndExp(inProgressBattle, 50.0, 30.0);

        battleFinishService.finishExpiredBattles(today);

        ArgumentCaptor<List<Battle>> captor = ArgumentCaptor.forClass(List.class);
        verify(battleRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue()).hasSize(2)
                .anyMatch(b -> b.battleStatus() == BattleStatus.DONE)
                .anyMatch(b -> b.battleStatus() == BattleStatus.CANCELED);
    }

    @Test
    @DisplayName("평균 exp가 높은 유저가 승자로 설정된다")
    void finishExpiredBattles_assignsWinnerWithHigherAvgExp() {
        LocalDate today = LocalDate.now();
        Battle inProgressBattle = new Battle(BATTLE_ID, Instant.now(), Instant.now(),
                today.minusDays(7), today.minusDays(1), BattleStatus.IN_PROGRESS, null, RIVAL_ID, 20L);

        when(battleRepositoryPort.findExpiredInProgressBattles(today)).thenReturn(List.of(inProgressBattle));
        when(battleRepositoryPort.findExpiredNotStartedBattles(today)).thenReturn(List.of());
        stubRivalAndExp(inProgressBattle, 80.0, 50.0);

        battleFinishService.finishExpiredBattles(today);

        ArgumentCaptor<List<Battle>> captor = ArgumentCaptor.forClass(List.class);
        verify(battleRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue())
                .filteredOn(b -> b.battleStatus() == BattleStatus.DONE)
                .allMatch(b -> FIRST_USER_ID.equals(b.winnerId()));
    }

    @Test
    @DisplayName("평균 exp가 동일하면 무승부로 winnerId가 null이다")
    void finishExpiredBattles_setsNullWinnerId_whenExpIsEqual() {
        LocalDate today = LocalDate.now();
        Battle inProgressBattle = new Battle(BATTLE_ID, Instant.now(), Instant.now(),
                today.minusDays(7), today.minusDays(1), BattleStatus.IN_PROGRESS, null, RIVAL_ID, 20L);

        when(battleRepositoryPort.findExpiredInProgressBattles(today)).thenReturn(List.of(inProgressBattle));
        when(battleRepositoryPort.findExpiredNotStartedBattles(today)).thenReturn(List.of());
        stubRivalAndExp(inProgressBattle, 50.0, 50.0);

        battleFinishService.finishExpiredBattles(today);

        ArgumentCaptor<List<Battle>> captor = ArgumentCaptor.forClass(List.class);
        verify(battleRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue())
                .filteredOn(b -> b.battleStatus() == BattleStatus.DONE)
                .allMatch(b -> b.winnerId() == null);
    }

    @Test
    @DisplayName("라이벌 조회 실패 시 해당 배틀을 건너뛰고 나머지 배틀은 정상 처리한다")
    void finishExpiredBattles_skipsFailedBattle_andProcessesRest() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(7);
        LocalDate endDate = today.minusDays(1);

        Long otherRivalId = 20L;
        Battle failBattle = new Battle(BATTLE_ID, Instant.now(), Instant.now(),
                startDate, endDate, BattleStatus.IN_PROGRESS, null, RIVAL_ID, 30L);
        Battle okBattle = new Battle(2L, Instant.now(), Instant.now(),
                startDate, endDate, BattleStatus.IN_PROGRESS, null, otherRivalId, 30L);

        when(battleRepositoryPort.findExpiredInProgressBattles(today)).thenReturn(List.of(failBattle, okBattle));
        when(battleRepositoryPort.findExpiredNotStartedBattles(today)).thenReturn(List.of());
        // RIVAL_ID는 rivalMap에 없음 (soft-delete 등으로 조회 불가)
        when(rivalRepositoryPort.findByIdIn(Set.of(RIVAL_ID, otherRivalId))).thenReturn(List.of(
                new Rival(otherRivalId, null, null, RivalLinkingStatus.ACCEPTED, FIRST_USER_ID, SECOND_USER_ID)
        ));
        when(userExpHistoryRepositoryPort.findAverageExpForBattles(eq(FIRST_USER_ID), any()))
                .thenReturn(Map.of(2L, 60.0));
        when(userExpHistoryRepositoryPort.findAverageExpForBattles(eq(SECOND_USER_ID), any()))
                .thenReturn(Map.of(2L, 40.0));

        battleFinishService.finishExpiredBattles(today);

        ArgumentCaptor<List<Battle>> captor = ArgumentCaptor.forClass(List.class);
        verify(battleRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue()).hasSize(1)
                .allMatch(b -> b.id().equals(2L) && b.battleStatus() == BattleStatus.DONE);
    }

    @Test
    @DisplayName("종료할 배틀이 없으면 saveAll을 호출하지 않는다")
    void finishExpiredBattles_doesNotCallSaveAll_whenNoBattlesToProcess() {
        LocalDate today = LocalDate.now();
        when(battleRepositoryPort.findExpiredInProgressBattles(today)).thenReturn(List.of());
        when(battleRepositoryPort.findExpiredNotStartedBattles(today)).thenReturn(List.of());

        battleFinishService.finishExpiredBattles(today);

        verify(battleRepositoryPort, never()).saveAll(any());
    }

    @Test
    @DisplayName("soft-delete된 라이벌의 배틀은 쿼리에서 제외되어 saveAll을 호출하지 않는다")
    void finishExpiredBattles_doesNotCallSaveAll_whenAllBattlesHaveSoftDeletedRival() {
        LocalDate today = LocalDate.now();
        when(battleRepositoryPort.findExpiredInProgressBattles(today)).thenReturn(List.of());
        when(battleRepositoryPort.findExpiredNotStartedBattles(today)).thenReturn(List.of());

        battleFinishService.finishExpiredBattles(today);

        verify(battleRepositoryPort, never()).saveAll(any());
    }

    private void stubRivalAndExp(Battle battle, double firstUserExp, double secondUserExp) {
        when(rivalRepositoryPort.findByIdIn(Set.of(battle.rivalId()))).thenReturn(List.of(
                new Rival(battle.rivalId(), null, null, RivalLinkingStatus.ACCEPTED, FIRST_USER_ID, SECOND_USER_ID)
        ));
        when(userExpHistoryRepositoryPort.findAverageExpForBattles(eq(FIRST_USER_ID), any()))
                .thenReturn(Map.of(battle.id(), firstUserExp));
        when(userExpHistoryRepositoryPort.findAverageExpForBattles(eq(SECOND_USER_ID), any()))
                .thenReturn(Map.of(battle.id(), secondUserExp));
    }
}