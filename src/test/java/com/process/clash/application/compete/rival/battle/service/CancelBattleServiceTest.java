package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.realtime.CompeteRefetchNotifier;
import com.process.clash.application.compete.rival.battle.data.ModifyBattleData;
import com.process.clash.application.compete.rival.battle.exception.exception.badrequest.CancelBattleInvalidStatusException;
import com.process.clash.application.compete.rival.battle.exception.exception.forbidden.CancelBattleForbiddenException;
import com.process.clash.application.compete.rival.battle.exception.exception.notfound.BattleNotFoundException;
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
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancelBattleServiceTest {

    @Mock
    private BattleRepositoryPort battleRepositoryPort;

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    @Mock
    private UserNoticeRepositoryPort userNoticeRepositoryPort;

    @Mock
    private CompeteRefetchNotifier competeRefetchNotifier;

    private CancelBattleService cancelBattleService;

    @BeforeEach
    void setUp() {
        cancelBattleService = new CancelBattleService(
                battleRepositoryPort,
                rivalRepositoryPort,
                userNoticeRepositoryPort,
                competeRefetchNotifier
        );
    }

    @Test
    @DisplayName("배틀 취소 시 배틀 상태가 CANCELED로 변경된다")
    void execute_changesBattleStatusToCanceled() {
        Actor actor = new Actor(1L);
        Long battleId = 20L;
        Long rivalId = 30L;
        Long opponentId = 2L;
        Battle battle = new Battle(battleId, Instant.now(), Instant.now(),
                LocalDate.now(), LocalDate.now().plusDays(7),
                BattleStatus.PENDING, null, rivalId, actor.id());

        when(battleRepositoryPort.findById(battleId)).thenReturn(Optional.of(battle));
        when(battleRepositoryPort.save(any(Battle.class))).thenAnswer(inv -> inv.getArgument(0));
        when(rivalRepositoryPort.findOpponentIdByIdAndUserId(rivalId, actor.id())).thenReturn(opponentId);
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(inv -> inv.getArgument(0));

        cancelBattleService.execute(new ModifyBattleData.Command(actor, battleId));

        ArgumentCaptor<Battle> battleCaptor = ArgumentCaptor.forClass(Battle.class);
        verify(battleRepositoryPort).save(battleCaptor.capture());
        assertThat(battleCaptor.getValue().battleStatus()).isEqualTo(BattleStatus.CANCELED);
    }

    @Test
    @DisplayName("배틀 취소 시 기존 APPLY_BATTLE 알림을 soft delete한다")
    void execute_softDeletesApplyBattleNotice() {
        Actor actor = new Actor(1L);
        Long battleId = 20L;
        Long rivalId = 30L;
        Long opponentId = 2L;
        Battle battle = new Battle(battleId, Instant.now(), Instant.now(),
                LocalDate.now(), LocalDate.now().plusDays(7),
                BattleStatus.PENDING, null, rivalId, actor.id());

        when(battleRepositoryPort.findById(battleId)).thenReturn(Optional.of(battle));
        when(battleRepositoryPort.save(any(Battle.class))).thenAnswer(inv -> inv.getArgument(0));
        when(rivalRepositoryPort.findOpponentIdByIdAndUserId(rivalId, actor.id())).thenReturn(opponentId);
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(inv -> inv.getArgument(0));

        cancelBattleService.execute(new ModifyBattleData.Command(actor, battleId));

        verify(userNoticeRepositoryPort).deleteApplyBattleNoticeByBattleId(battleId);
    }

    @Test
    @DisplayName("배틀 취소 시 상대방에게 CANCEL_BATTLE 알림을 전송한다")
    void execute_sendsCancelBattleNoticeToOpponent() {
        Actor actor = new Actor(1L);
        Long battleId = 20L;
        Long rivalId = 30L;
        Long opponentId = 2L;
        Battle battle = new Battle(battleId, Instant.now(), Instant.now(),
                LocalDate.now(), LocalDate.now().plusDays(7),
                BattleStatus.PENDING, null, rivalId, actor.id());

        when(battleRepositoryPort.findById(battleId)).thenReturn(Optional.of(battle));
        when(battleRepositoryPort.save(any(Battle.class))).thenAnswer(inv -> inv.getArgument(0));
        when(rivalRepositoryPort.findOpponentIdByIdAndUserId(rivalId, actor.id())).thenReturn(opponentId);
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(inv -> inv.getArgument(0));

        cancelBattleService.execute(new ModifyBattleData.Command(actor, battleId));

        verify(userNoticeRepositoryPort).save(any(UserNotice.class));
    }

    @Test
    @DisplayName("배틀 취소 시 상대방에게만 알림 소켓 이벤트를 전송한다")
    void execute_notifiesOnlyOpponent() {
        Actor actor = new Actor(1L);
        Long battleId = 20L;
        Long rivalId = 30L;
        Long opponentId = 2L;
        Battle battle = new Battle(battleId, Instant.now(), Instant.now(),
                LocalDate.now(), LocalDate.now().plusDays(7),
                BattleStatus.PENDING, null, rivalId, actor.id());

        when(battleRepositoryPort.findById(battleId)).thenReturn(Optional.of(battle));
        when(battleRepositoryPort.save(any(Battle.class))).thenAnswer(inv -> inv.getArgument(0));
        when(rivalRepositoryPort.findOpponentIdByIdAndUserId(rivalId, actor.id())).thenReturn(opponentId);
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(inv -> inv.getArgument(0));

        cancelBattleService.execute(new ModifyBattleData.Command(actor, battleId));

        verify(competeRefetchNotifier).notifyUserNoticeChanged(List.of(opponentId));

        ArgumentCaptor<Collection<Long>> competeCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(competeRefetchNotifier).notifyCompeteChanged(competeCaptor.capture());
        assertThat(competeCaptor.getValue()).containsExactlyInAnyOrder(opponentId, actor.id());
    }

    @Test
    @DisplayName("배틀 신청자가 아닌 경우 CancelBattleForbiddenException이 발생한다")
    void execute_throwsWhenNotApplicant() {
        Actor actor = new Actor(1L);
        Long battleId = 20L;
        Long rivalId = 30L;
        Long actualApplicantId = 2L;
        // applicantId가 다른 유저(2L)로 설정
        Battle battle = new Battle(battleId, Instant.now(), Instant.now(),
                LocalDate.now(), LocalDate.now().plusDays(7),
                BattleStatus.PENDING, null, rivalId, actualApplicantId);

        when(battleRepositoryPort.findById(battleId)).thenReturn(Optional.of(battle));

        assertThatThrownBy(() -> cancelBattleService.execute(new ModifyBattleData.Command(actor, battleId)))
                .isInstanceOf(CancelBattleForbiddenException.class);

        verify(battleRepositoryPort, never()).save(any());
        verify(userNoticeRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("PENDING 상태가 아닌 배틀을 취소하면 CancelBattleInvalidStatusException이 발생한다")
    void execute_throwsWhenStatusIsNotPending() {
        Actor actor = new Actor(1L);
        Long battleId = 20L;
        Long rivalId = 30L;
        Battle battle = new Battle(battleId, Instant.now(), Instant.now(),
                LocalDate.now(), LocalDate.now().plusDays(7),
                BattleStatus.IN_PROGRESS, null, rivalId, actor.id());

        when(battleRepositoryPort.findById(battleId)).thenReturn(Optional.of(battle));

        assertThatThrownBy(() -> cancelBattleService.execute(new ModifyBattleData.Command(actor, battleId)))
                .isInstanceOf(CancelBattleInvalidStatusException.class);

        verify(battleRepositoryPort, never()).save(any());
        verify(userNoticeRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("존재하지 않는 배틀 ID로 취소하면 BattleNotFoundException이 발생한다")
    void execute_throwsWhenBattleNotFound() {
        Actor actor = new Actor(1L);
        Long battleId = 99L;

        when(battleRepositoryPort.findById(battleId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cancelBattleService.execute(new ModifyBattleData.Command(actor, battleId)))
                .isInstanceOf(BattleNotFoundException.class);

        verify(battleRepositoryPort, never()).save(any());
        verify(userNoticeRepositoryPort, never()).save(any());
        verify(competeRefetchNotifier, never()).notifyUserNoticeChanged(any());
    }
}
