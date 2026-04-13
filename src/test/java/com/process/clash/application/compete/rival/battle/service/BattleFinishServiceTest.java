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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BattleFinishServiceTest {

    private static final Long RIVAL_ID = 10L;
    private static final Long FIRST_USER_ID = 20L;
    private static final Long SECOND_USER_ID = 30L;

    @Mock
    private BattleRepositoryPort battleRepositoryPort;

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    @Mock
    private UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;

    private BattleFinishService battleFinishService;

    @BeforeEach
    void setUp() {
        battleFinishService = new BattleFinishService(
                battleRepositoryPort,
                rivalRepositoryPort,
                userExpHistoryRepositoryPort
        );
    }

    @Test
    @DisplayName("종료 시각이 지난 IN_PROGRESS 배틀을 DONE으로 전환한다")
    void finishExpiredBattles_transitionsInProgressToDone() {
        Instant startedAt = Instant.now().minus(8, ChronoUnit.DAYS);
        Instant endAt = Instant.now().minus(1, ChronoUnit.DAYS);
        Battle inProgressBattle = new Battle(1L, Instant.now(), Instant.now(),
                startedAt, endAt, 7, BattleStatus.IN_PROGRESS, null, RIVAL_ID, FIRST_USER_ID);

        Rival rival = new Rival(RIVAL_ID, Instant.now(), Instant.now(),
                RivalLinkingStatus.ACCEPTED, FIRST_USER_ID, SECOND_USER_ID);

        when(battleRepositoryPort.findExpiredInProgressBattles()).thenReturn(List.of(inProgressBattle));
        when(rivalRepositoryPort.findByIdIn(Set.of(RIVAL_ID))).thenReturn(List.of(rival));
        when(userExpHistoryRepositoryPort.findAverageExpForBattles(eq(FIRST_USER_ID), anyList()))
                .thenReturn(Map.of(1L, 10.0));
        when(userExpHistoryRepositoryPort.findAverageExpForBattles(eq(SECOND_USER_ID), anyList()))
                .thenReturn(Map.of(1L, 5.0));

        battleFinishService.finishExpiredBattles();

        ArgumentCaptor<List<Battle>> captor = ArgumentCaptor.forClass(List.class);
        verify(battleRepositoryPort).saveAll(captor.capture());
        assertThat(captor.getValue()).allMatch(b -> b.battleStatus() == BattleStatus.DONE);
    }

    @Test
    @DisplayName("종료할 배틀이 없으면 saveAll을 호출하지 않는다")
    void finishExpiredBattles_doesNotCallSaveAll_whenNoBattlesToProcess() {
        when(battleRepositoryPort.findExpiredInProgressBattles()).thenReturn(List.of());

        battleFinishService.finishExpiredBattles();

        verify(battleRepositoryPort, never()).saveAll(any());
    }
}