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
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
    @DisplayName("종료 시각이 지난 IN_PROGRESS 배틀을 DONE으로 전환한다")
    void finishExpiredBattles_transitionsInProgressToDone() {
        Instant startedAt = Instant.now().minus(8, ChronoUnit.DAYS);
        Instant endAt = Instant.now().minus(1, ChronoUnit.DAYS);
        Battle inProgressBattle = new Battle(1L, Instant.now(), Instant.now(),
                startedAt, endAt, 7, BattleStatus.IN_PROGRESS, null, 10L, 20L);

        when(battleRepositoryPort.findExpiredInProgressBattles()).thenReturn(List.of(inProgressBattle));
        when(battleRepositoryPort.findExpiredNotStartedBattles()).thenReturn(List.of());

        battleFinishService.finishExpiredBattles();

        ArgumentCaptor<List<Battle>> captor = ArgumentCaptor.forClass(List.class);
        verify(battleRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue()).allMatch(b -> b.battleStatus() == BattleStatus.DONE);
    }

    @Test
    @DisplayName("종료 시각이 지난 NOT_STARTED 배틀을 CANCELED로 전환한다")
    void finishExpiredBattles_transitionsNotStartedToCanceled() {
        Instant startedAt = Instant.now().minus(8, ChronoUnit.DAYS);
        Instant endAt = Instant.now().minus(1, ChronoUnit.DAYS);
        Battle notStartedBattle = new Battle(2L, Instant.now(), Instant.now(),
                startedAt, endAt, 7, BattleStatus.NOT_STARTED, null, 10L, 20L);

        when(battleRepositoryPort.findExpiredInProgressBattles()).thenReturn(List.of());
        when(battleRepositoryPort.findExpiredNotStartedBattles()).thenReturn(List.of(notStartedBattle));

        battleFinishService.finishExpiredBattles();

        ArgumentCaptor<List<Battle>> captor = ArgumentCaptor.forClass(List.class);
        verify(battleRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue()).allMatch(b -> b.battleStatus() == BattleStatus.CANCELED);
    }

    @Test
    @DisplayName("IN_PROGRESS와 NOT_STARTED 만료 배틀을 한 번의 saveAll로 처리한다")
    void finishExpiredBattles_savesAllInSingleCall() {
        Instant startedAt = Instant.now().minus(8, ChronoUnit.DAYS);
        Instant endAt = Instant.now().minus(1, ChronoUnit.DAYS);
        Battle inProgressBattle = new Battle(1L, Instant.now(), Instant.now(),
                startedAt, endAt, 7, BattleStatus.IN_PROGRESS, null, 10L, 20L);
        Battle notStartedBattle = new Battle(2L, Instant.now(), Instant.now(),
                startedAt, endAt, 7, BattleStatus.NOT_STARTED, null, 10L, 20L);

        when(battleRepositoryPort.findExpiredInProgressBattles()).thenReturn(List.of(inProgressBattle));
        when(battleRepositoryPort.findExpiredNotStartedBattles()).thenReturn(List.of(notStartedBattle));

        battleFinishService.finishExpiredBattles();

        ArgumentCaptor<List<Battle>> captor = ArgumentCaptor.forClass(List.class);
        verify(battleRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue()).hasSize(2)
                .anyMatch(b -> b.battleStatus() == BattleStatus.DONE)
                .anyMatch(b -> b.battleStatus() == BattleStatus.CANCELED);
    }

    @Test
    @DisplayName("종료할 배틀이 없으면 saveAll을 호출하지 않는다")
    void finishExpiredBattles_doesNotCallSaveAll_whenNoBattlesToProcess() {
        when(battleRepositoryPort.findExpiredInProgressBattles()).thenReturn(List.of());
        when(battleRepositoryPort.findExpiredNotStartedBattles()).thenReturn(List.of());

        battleFinishService.finishExpiredBattles();

        verify(battleRepositoryPort, never()).saveAll(any());
    }
}