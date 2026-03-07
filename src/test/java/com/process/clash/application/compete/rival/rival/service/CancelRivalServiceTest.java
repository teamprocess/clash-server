package com.process.clash.application.compete.rival.rival.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.realtime.CompeteRefetchNotifier;
import com.process.clash.application.compete.rival.rival.data.ModifyRivalData;
import com.process.clash.application.compete.rival.rival.exception.exception.badrequet.CancelRivalInvalidStatusException;
import com.process.clash.application.compete.rival.rival.exception.exception.forbidden.CancelRivalForbiddenException;
import com.process.clash.application.compete.rival.rival.exception.exception.notfound.RivalNotFoundException;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.usernotice.port.out.UserNoticeRepositoryPort;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.rival.rival.enums.RivalLinkingStatus;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancelRivalServiceTest {

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    @Mock
    private UserNoticeRepositoryPort userNoticeRepositoryPort;

    @Mock
    private CompeteRefetchNotifier competeRefetchNotifier;

    private CancelRivalService cancelRivalService;

    @BeforeEach
    void setUp() {
        cancelRivalService = new CancelRivalService(
                rivalRepositoryPort,
                userNoticeRepositoryPort,
                competeRefetchNotifier
        );
    }

    @Test
    @DisplayName("라이벌 취소 시 라이벌 상태가 CANCELED로 변경된다")
    void execute_changesBattleStatusToCanceled() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, actor.id(), opponentId);

        when(rivalRepositoryPort.findById(rivalId)).thenReturn(Optional.of(rival));
        when(rivalRepositoryPort.save(any(Rival.class))).thenAnswer(inv -> inv.getArgument(0));
        when(rivalRepositoryPort.findOpponentIdByIdAndUserIdInRejectCase(rivalId, actor.id())).thenReturn(opponentId);
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(inv -> inv.getArgument(0));

        cancelRivalService.execute(ModifyRivalData.Command.of(actor, rivalId));

        ArgumentCaptor<Rival> rivalCaptor = ArgumentCaptor.forClass(Rival.class);
        verify(rivalRepositoryPort).save(rivalCaptor.capture());
        assertThat(rivalCaptor.getValue().rivalLinkingStatus()).isEqualTo(RivalLinkingStatus.CANCELED);
    }

    @Test
    @DisplayName("라이벌 취소 시 기존 APPLY_RIVAL 알림을 soft delete한다")
    void execute_softDeletesApplyRivalNotice() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, actor.id(), opponentId);

        when(rivalRepositoryPort.findById(rivalId)).thenReturn(Optional.of(rival));
        when(rivalRepositoryPort.save(any(Rival.class))).thenAnswer(inv -> inv.getArgument(0));
        when(rivalRepositoryPort.findOpponentIdByIdAndUserIdInRejectCase(rivalId, actor.id())).thenReturn(opponentId);
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(inv -> inv.getArgument(0));

        cancelRivalService.execute(ModifyRivalData.Command.of(actor, rivalId));

        verify(userNoticeRepositoryPort).deleteApplyRivalNoticeByRivalId(rivalId);
    }

    @Test
    @DisplayName("라이벌 취소 시 상대방에게 CANCEL_RIVAL 알림을 전송한다")
    void execute_sendsCancelRivalNoticeToOpponent() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, actor.id(), opponentId);

        when(rivalRepositoryPort.findById(rivalId)).thenReturn(Optional.of(rival));
        when(rivalRepositoryPort.save(any(Rival.class))).thenAnswer(inv -> inv.getArgument(0));
        when(rivalRepositoryPort.findOpponentIdByIdAndUserIdInRejectCase(rivalId, actor.id())).thenReturn(opponentId);
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(inv -> inv.getArgument(0));

        cancelRivalService.execute(ModifyRivalData.Command.of(actor, rivalId));

        verify(userNoticeRepositoryPort).save(any(UserNotice.class));
    }

    @Test
    @DisplayName("라이벌 취소 시 상대방에게만 알림 소켓 이벤트를 전송한다")
    void execute_notifiesOnlyOpponent() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, actor.id(), opponentId);

        when(rivalRepositoryPort.findById(rivalId)).thenReturn(Optional.of(rival));
        when(rivalRepositoryPort.save(any(Rival.class))).thenAnswer(inv -> inv.getArgument(0));
        when(rivalRepositoryPort.findOpponentIdByIdAndUserIdInRejectCase(rivalId, actor.id())).thenReturn(opponentId);
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(inv -> inv.getArgument(0));

        cancelRivalService.execute(ModifyRivalData.Command.of(actor, rivalId));

        verify(competeRefetchNotifier).notifyUserNoticeChanged(List.of(opponentId));

        ArgumentCaptor<Collection<Long>> competeCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(competeRefetchNotifier).notifyCompeteChanged(competeCaptor.capture());
        assertThat(competeCaptor.getValue()).containsExactlyInAnyOrder(opponentId, actor.id());
    }

    @Test
    @DisplayName("라이벌 신청자가 아닌 경우 CancelRivalForbiddenException이 발생한다")
    void execute_throwsWhenNotApplicant() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        // firstUserId가 opponentId(2L)로 설정되어 있어 actor(1L)는 신청자가 아님
        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, opponentId, actor.id());

        when(rivalRepositoryPort.findById(rivalId)).thenReturn(Optional.of(rival));

        assertThatThrownBy(() -> cancelRivalService.execute(ModifyRivalData.Command.of(actor, rivalId)))
                .isInstanceOf(CancelRivalForbiddenException.class);

        verify(rivalRepositoryPort, never()).save(any());
        verify(userNoticeRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("PENDING 상태가 아닌 라이벌을 취소하면 CancelRivalInvalidStatusException이 발생한다")
    void execute_throwsWhenStatusIsNotPending() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(), RivalLinkingStatus.ACCEPTED, actor.id(), opponentId);

        when(rivalRepositoryPort.findById(rivalId)).thenReturn(Optional.of(rival));

        assertThatThrownBy(() -> cancelRivalService.execute(ModifyRivalData.Command.of(actor, rivalId)))
                .isInstanceOf(CancelRivalInvalidStatusException.class);

        verify(rivalRepositoryPort, never()).save(any());
        verify(userNoticeRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("존재하지 않는 라이벌 ID로 취소하면 RivalNotFoundException이 발생한다")
    void execute_throwsWhenRivalNotFound() {
        Actor actor = new Actor(1L);
        Long rivalId = 99L;

        when(rivalRepositoryPort.findById(rivalId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cancelRivalService.execute(ModifyRivalData.Command.of(actor, rivalId)))
                .isInstanceOf(RivalNotFoundException.class);

        verify(userNoticeRepositoryPort, never()).save(any());
        verify(competeRefetchNotifier, never()).notifyUserNoticeChanged(any());
    }
}
