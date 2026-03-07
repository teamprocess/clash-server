package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.realtime.CompeteRefetchNotifier;
import com.process.clash.application.compete.rival.battle.data.ModifyBattleData;
import com.process.clash.application.compete.rival.battle.policy.ModifyBattlePolicy;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.usernotice.port.out.UserNoticeRepositoryPort;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.rival.battle.enums.BattleStatus;
import com.process.clash.domain.user.usernotice.entity.UserNotice;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AcceptBattleServiceTest {

    @Mock
    private BattleRepositoryPort battleRepositoryPort;

    @Mock
    private ModifyBattlePolicy modifyBattlePolicy;

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    @Mock
    private UserNoticeRepositoryPort userNoticeRepositoryPort;

    @Mock
    private CompeteRefetchNotifier competeRefetchNotifier;

    private AcceptBattleService acceptBattleService;

    @BeforeEach
    void setUp() {
        acceptBattleService = new AcceptBattleService(
            battleRepositoryPort,
            modifyBattlePolicy,
            rivalRepositoryPort,
            userNoticeRepositoryPort,
            competeRefetchNotifier
        );
    }

    @Test
    @DisplayName("배틀 수락 시 상대방에게만 알림 소켓 이벤트를 전송한다")
    void execute_notifiesOnlyOpponentOnAccept() {
        Actor actor = new Actor(1L);
        Long battleId = 20L;
        Long rivalId = 30L;
        Long opponentId = 2L;
        Battle battle = new Battle(
            battleId,
            Instant.now(),
            Instant.now(),
            LocalDate.now(),
            LocalDate.now().plusDays(1),
            BattleStatus.PENDING,
            null,
            rivalId,
            opponentId
        );

        when(battleRepositoryPort.findById(battleId)).thenReturn(Optional.of(battle));
        when(battleRepositoryPort.save(any(Battle.class))).thenReturn(battle.accept());
        when(rivalRepositoryPort.findOpponentIdByIdAndUserId(rivalId, actor.id())).thenReturn(opponentId);
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        acceptBattleService.execute(new ModifyBattleData.Command(actor, battleId));

        verify(competeRefetchNotifier).notifyUserNoticeChanged(java.util.List.of(opponentId));

        ArgumentCaptor<Collection<Long>> competeCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(competeRefetchNotifier).notifyCompeteChanged(competeCaptor.capture());
        assertThat(competeCaptor.getValue()).containsExactlyInAnyOrder(opponentId, actor.id());
    }

    @Test
    @DisplayName("배틀 수락 시 상대방에게만 알림 1개를 저장한다")
    void execute_savesOneNoticeOnAccept() {
        Actor actor = new Actor(1L);
        Long battleId = 20L;
        Long rivalId = 30L;
        Long opponentId = 2L;
        Battle battle = new Battle(
            battleId,
            Instant.now(),
            Instant.now(),
            LocalDate.now(),
            LocalDate.now().plusDays(1),
            BattleStatus.PENDING,
            null,
            rivalId,
            opponentId
        );

        when(battleRepositoryPort.findById(battleId)).thenReturn(Optional.of(battle));
        when(battleRepositoryPort.save(any(Battle.class))).thenReturn(battle.accept());
        when(rivalRepositoryPort.findOpponentIdByIdAndUserId(rivalId, actor.id())).thenReturn(opponentId);
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        acceptBattleService.execute(new ModifyBattleData.Command(actor, battleId));

        verify(userNoticeRepositoryPort, times(1)).save(any(UserNotice.class));
    }

    @Test
    @DisplayName("배틀 수락 시 기존 APPLY_BATTLE 알림을 soft delete한다")
    void execute_softDeletesApplyBattleNoticeOnAccept() {
        Actor actor = new Actor(1L);
        Long battleId = 20L;
        Long rivalId = 30L;
        Long opponentId = 2L;
        Battle battle = new Battle(
            battleId,
            Instant.now(),
            Instant.now(),
            LocalDate.now(),
            LocalDate.now().plusDays(1),
            BattleStatus.PENDING,
            null,
            rivalId,
            opponentId
        );

        when(battleRepositoryPort.findById(battleId)).thenReturn(Optional.of(battle));
        when(battleRepositoryPort.save(any(Battle.class))).thenReturn(battle.accept());
        when(rivalRepositoryPort.findOpponentIdByIdAndUserId(rivalId, actor.id())).thenReturn(opponentId);
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        acceptBattleService.execute(new ModifyBattleData.Command(actor, battleId));

        verify(userNoticeRepositoryPort).deleteApplyBattleNoticeByBattleId(battleId);
    }
}
