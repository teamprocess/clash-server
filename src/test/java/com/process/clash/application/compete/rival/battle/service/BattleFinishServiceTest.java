package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.rival.battle.enums.BattleStatus;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BattleFinishServiceTest {

    @Mock
    private BattleRepositoryPort battleRepositoryPort;

    private BattleFinishService battleFinishService;

    @BeforeEach
    void setUp() {
        battleFinishService = new BattleFinishService(battleRepositoryPort);
    }

    @Test
    @DisplayName("종료일이 지난 IN_PROGRESS 배틀을 DONE으로 전환한다")
    void finishExpiredBattles_transitionsInProgressToDone() {
        LocalDate today = LocalDate.now();
        Battle inProgressBattle = new Battle(1L, Instant.now(), Instant.now(),
                today.minusDays(7), today.minusDays(1), BattleStatus.IN_PROGRESS, null, 10L, 20L);

        when(battleRepositoryPort.findExpiredInProgressBattles(today)).thenReturn(List.of(inProgressBattle));
        when(battleRepositoryPort.findExpiredNotStartedBattles(today)).thenReturn(List.of());

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
                today.minusDays(7), today.minusDays(1), BattleStatus.NOT_STARTED, null, 10L, 20L);

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
        Battle inProgressBattle = new Battle(1L, Instant.now(), Instant.now(),
                today.minusDays(7), today.minusDays(1), BattleStatus.IN_PROGRESS, null, 10L, 20L);
        Battle notStartedBattle = new Battle(2L, Instant.now(), Instant.now(),
                today.minusDays(7), today.minusDays(1), BattleStatus.NOT_STARTED, null, 10L, 20L);

        when(battleRepositoryPort.findExpiredInProgressBattles(today)).thenReturn(List.of(inProgressBattle));
        when(battleRepositoryPort.findExpiredNotStartedBattles(today)).thenReturn(List.of(notStartedBattle));

        battleFinishService.finishExpiredBattles(today);

        ArgumentCaptor<List<Battle>> captor = ArgumentCaptor.forClass(List.class);
        verify(battleRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue()).hasSize(2)
                .anyMatch(b -> b.battleStatus() == BattleStatus.DONE)
                .anyMatch(b -> b.battleStatus() == BattleStatus.CANCELED);
    }

    @Test
    @DisplayName("종료할 배틀이 없으면 saveAll을 호출하지 않는다")
    void finishExpiredBattles_doesNotCallSaveAll_whenNoBattlesToProcess() {
        LocalDate today = LocalDate.now();
        when(battleRepositoryPort.findExpiredInProgressBattles(today)).thenReturn(List.of());
        when(battleRepositoryPort.findExpiredNotStartedBattles(today)).thenReturn(List.of());

        battleFinishService.finishExpiredBattles(today);

        verify(battleRepositoryPort, never()).saveAll(List.of());
    }
}