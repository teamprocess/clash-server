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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BattleStartServiceTest {

    @Mock
    private BattleRepositoryPort battleRepositoryPort;

    private BattleStartService battleStartService;

    @BeforeEach
    void setUp() {
        battleStartService = new BattleStartService(battleRepositoryPort);
    }

    @Test
    @DisplayName("시작일이 도래한 NOT_STARTED 배틀을 IN_PROGRESS로 전환한다")
    void startScheduledBattles_transitionsNotStartedToInProgress() {
        LocalDate today = LocalDate.now();
        Battle notStartedBattle = new Battle(1L, Instant.now(), Instant.now(),
                today, today.plusDays(7), BattleStatus.NOT_STARTED, null, 10L, 20L);

        when(battleRepositoryPort.findNotStartedBattlesToStart(today)).thenReturn(List.of(notStartedBattle));
        when(battleRepositoryPort.saveAll(any())).thenReturn(List.of(notStartedBattle.start()));

        battleStartService.startScheduledBattles(today);

        ArgumentCaptor<List<Battle>> captor = ArgumentCaptor.forClass(List.class);
        verify(battleRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue()).allMatch(b -> b.battleStatus() == BattleStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("시작할 배틀이 없으면 saveAll을 빈 리스트로 호출한다")
    void startScheduledBattles_savesEmptyList_whenNoBattlesToStart() {
        LocalDate today = LocalDate.now();
        when(battleRepositoryPort.findNotStartedBattlesToStart(today)).thenReturn(List.of());
        when(battleRepositoryPort.saveAll(List.of())).thenReturn(List.of());

        battleStartService.startScheduledBattles(today);

        verify(battleRepositoryPort).saveAll(List.of());
    }

    @Test
    @DisplayName("soft-delete된 라이벌의 배틀은 조회에서 제외되어 빈 리스트로 saveAll을 호출한다")
    void startScheduledBattles_savesEmptyList_whenAllBattlesHaveSoftDeletedRival() {
        LocalDate today = LocalDate.now();
        // fk_rival_id IS NOT NULL 조건으로 soft-delete된 라이벌의 배틀은 이미 쿼리에서 필터링됨
        when(battleRepositoryPort.findNotStartedBattlesToStart(today)).thenReturn(List.of());
        when(battleRepositoryPort.saveAll(List.of())).thenReturn(List.of());

        battleStartService.startScheduledBattles(today);

        verify(battleRepositoryPort).saveAll(List.of());
    }
}