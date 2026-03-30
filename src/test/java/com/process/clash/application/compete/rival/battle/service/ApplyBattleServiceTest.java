package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.realtime.CompeteRefetchNotifier;
import com.process.clash.application.compete.rival.battle.data.ApplyBattleData;
import com.process.clash.application.compete.rival.battle.policy.ApplyBattlePolicy;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.usernotice.port.out.UserNoticeRepositoryPort;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.rival.battle.enums.BattleStatus;
import com.process.clash.domain.user.usernotice.entity.UserNotice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplyBattleServiceTest {

    @Mock
    private BattleRepositoryPort battleRepositoryPort;

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    @Mock
    private UserNoticeRepositoryPort userNoticeRepositoryPort;

    @Mock
    private ApplyBattlePolicy applyBattlePolicy;

    @Mock
    private CompeteRefetchNotifier competeRefetchNotifier;

    private ApplyBattleService applyBattleService;

    @BeforeEach
    void setUp() {
        applyBattleService = new ApplyBattleService(
            battleRepositoryPort,
            rivalRepositoryPort,
            userNoticeRepositoryPort,
            applyBattlePolicy,
            competeRefetchNotifier
        );
    }

    @Test
    @DisplayName("배틀 신청 시 알림 변경과 경쟁 데이터 변경 소켓 이벤트를 전송한다")
    void execute_notifiesNoticeAndCompeteChangeOnApply() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        ApplyBattleData.Command command = new ApplyBattleData.Command(actor, rivalId, 7);
        Battle savedBattle = pendingBattle(100L, rivalId, actor.id(), 7);

        when(rivalRepositoryPort.findOpponentIdByIdAndUserId(rivalId, actor.id())).thenReturn(opponentId);
        when(battleRepositoryPort.save(any(Battle.class))).thenReturn(savedBattle);
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        applyBattleService.execute(command);

        verify(competeRefetchNotifier).notifyUserNoticeChanged(java.util.List.of(opponentId));
        ArgumentCaptor<Collection<Long>> competeCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(competeRefetchNotifier).notifyCompeteChanged(competeCaptor.capture());
        assertThat(competeCaptor.getValue()).containsExactlyInAnyOrder(actor.id(), opponentId);
    }

    @Test
    @DisplayName("배틀 신청 시 duration이 Battle에 올바르게 저장된다")
    void execute_setsDurationCorrectly() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        int duration = 7;
        ApplyBattleData.Command command = new ApplyBattleData.Command(actor, rivalId, duration);
        Battle savedBattle = pendingBattle(100L, rivalId, actor.id(), duration);

        when(rivalRepositoryPort.findOpponentIdByIdAndUserId(rivalId, actor.id())).thenReturn(opponentId);
        when(battleRepositoryPort.save(any(Battle.class))).thenReturn(savedBattle);
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        applyBattleService.execute(command);

        ArgumentCaptor<Battle> battleCaptor = ArgumentCaptor.forClass(Battle.class);
        verify(battleRepositoryPort).save(battleCaptor.capture());
        assertThat(battleCaptor.getValue().duration()).isEqualTo(duration);
        assertThat(battleCaptor.getValue().startedAt()).isNull();
        assertThat(battleCaptor.getValue().endAt()).isNull();
    }

    @Test
    @DisplayName("배틀 재신청 시 이전 CANCEL_BATTLE 알림을 soft delete한다")
    void execute_softDeletesCancelBattleNoticeOnReApply() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        ApplyBattleData.Command command = new ApplyBattleData.Command(actor, rivalId, 7);
        Battle savedBattle = pendingBattle(100L, rivalId, actor.id(), 7);

        when(rivalRepositoryPort.findOpponentIdByIdAndUserId(rivalId, actor.id())).thenReturn(opponentId);
        when(battleRepositoryPort.save(any(Battle.class))).thenReturn(savedBattle);
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        applyBattleService.execute(command);

        verify(userNoticeRepositoryPort).deleteCancelBattleNoticeBySenderAndReceiver(actor.id(), opponentId);
    }

    private Battle pendingBattle(Long battleId, Long rivalId, Long applicantId, int duration) {
        return new Battle(battleId, Instant.now(), Instant.now(),
                null, null, duration,
                BattleStatus.PENDING, null, rivalId, applicantId);
    }
}