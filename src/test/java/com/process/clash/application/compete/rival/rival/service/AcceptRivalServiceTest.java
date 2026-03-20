package com.process.clash.application.compete.rival.rival.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.realtime.CompeteRefetchNotifier;
import com.process.clash.application.compete.rival.rival.data.ModifyRivalData;
import com.process.clash.application.compete.rival.rival.policy.AcceptRivalPolicy;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AcceptRivalServiceTest {

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    @Mock
    private AcceptRivalPolicy acceptRivalPolicy;

    @Mock
    private UserNoticeRepositoryPort userNoticeRepositoryPort;

    @Mock
    private CompeteRefetchNotifier competeRefetchNotifier;

    private AcceptRivalService acceptRivalService;

    @BeforeEach
    void setUp() {
        acceptRivalService = new AcceptRivalService(
                rivalRepositoryPort,
                acceptRivalPolicy,
                userNoticeRepositoryPort,
                competeRefetchNotifier
        );
    }

    // ===== 기본 수락 케이스 (자동 취소 없음) =====

    @Test
    @DisplayName("라이벌 수락 시 양쪽 모두에게 소켓 이벤트를 전송한다")
    void execute_notifiesBothUsersOnAccept() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, opponentId, actor.id());

        when(acceptRivalPolicy.check(actor, rivalId)).thenReturn(rival);
        when(rivalRepositoryPort.save(any(Rival.class))).thenReturn(rival.accept());
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(inv -> inv.getArgument(0));
        // countAcceptedByUserId 기본값 0 → 4명 미만 → 자동 취소 없음

        acceptRivalService.execute(ModifyRivalData.Command.of(actor, rivalId));

        ArgumentCaptor<Collection<Long>> noticeCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(competeRefetchNotifier).notifyUserNoticeChanged(noticeCaptor.capture());
        assertThat(noticeCaptor.getValue()).containsExactlyInAnyOrder(opponentId, actor.id());

        ArgumentCaptor<Collection<Long>> competeCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(competeRefetchNotifier).notifyCompeteChanged(competeCaptor.capture());
        assertThat(competeCaptor.getValue()).containsExactlyInAnyOrder(opponentId, actor.id());
    }

    @Test
    @DisplayName("라이벌 수락 시 상대방에게 ACCEPT_RIVAL 알림 1개를 저장한다")
    void execute_savesOneAcceptNoticeOnAccept() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, opponentId, actor.id());

        when(acceptRivalPolicy.check(actor, rivalId)).thenReturn(rival);
        when(rivalRepositoryPort.save(any(Rival.class))).thenReturn(rival.accept());
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(inv -> inv.getArgument(0));

        acceptRivalService.execute(ModifyRivalData.Command.of(actor, rivalId));

        verify(userNoticeRepositoryPort, times(1)).save(any(UserNotice.class));
        verify(userNoticeRepositoryPort, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("라이벌 수락 시 기존 APPLY_RIVAL 알림을 삭제한다")
    void execute_deletesApplyRivalNoticeOnAccept() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, opponentId, actor.id());

        when(acceptRivalPolicy.check(actor, rivalId)).thenReturn(rival);
        when(rivalRepositoryPort.save(any(Rival.class))).thenReturn(rival.accept());
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(inv -> inv.getArgument(0));

        acceptRivalService.execute(ModifyRivalData.Command.of(actor, rivalId));

        verify(userNoticeRepositoryPort).deleteApplyRivalNoticeByRivalId(rivalId);
    }

    // ===== 수락 후 ACCEPTED 4명 도달 → 자동 취소 케이스 =====

    @Test
    @DisplayName("수락 후 수락자가 ACCEPTED 4명이 되면 나머지 PENDING을 모두 취소한다")
    void execute_cancelsAllPendingWhenActorReachesFourAccepted() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        Long pendingOpponentId = 3L;

        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, opponentId, actor.id());
        Rival pendingRival = new Rival(20L, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, actor.id(), pendingOpponentId);

        when(acceptRivalPolicy.check(actor, rivalId)).thenReturn(rival);
        when(rivalRepositoryPort.save(any(Rival.class))).thenReturn(rival.accept());
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(inv -> inv.getArgument(0));
        when(rivalRepositoryPort.countAcceptedByUserId(actor.id())).thenReturn(4);
        when(rivalRepositoryPort.findAllPendingByUserId(actor.id())).thenReturn(List.of(pendingRival));
        // 신청자는 4명 미만 → 자동 취소 없음
        when(rivalRepositoryPort.countAcceptedByUserId(opponentId)).thenReturn(2);

        acceptRivalService.execute(ModifyRivalData.Command.of(actor, rivalId));

        verify(rivalRepositoryPort).saveAll(anyList());
        verify(userNoticeRepositoryPort).deleteApplyRivalNoticeByRivalId(pendingRival.id());
        verify(userNoticeRepositoryPort).saveAll(anyList());
    }

    @Test
    @DisplayName("수락 후 신청자가 ACCEPTED 4명이 되면 나머지 PENDING을 모두 취소한다")
    void execute_cancelsAllPendingWhenOpponentReachesFourAccepted() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        Long pendingOpponentId = 3L;

        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, opponentId, actor.id());
        Rival pendingRival = new Rival(20L, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, opponentId, pendingOpponentId);

        when(acceptRivalPolicy.check(actor, rivalId)).thenReturn(rival);
        when(rivalRepositoryPort.save(any(Rival.class))).thenReturn(rival.accept());
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(inv -> inv.getArgument(0));
        // 수락자는 4명 미만 → 자동 취소 없음
        when(rivalRepositoryPort.countAcceptedByUserId(actor.id())).thenReturn(2);
        when(rivalRepositoryPort.countAcceptedByUserId(opponentId)).thenReturn(4);
        when(rivalRepositoryPort.findAllPendingByUserId(opponentId)).thenReturn(List.of(pendingRival));

        acceptRivalService.execute(ModifyRivalData.Command.of(actor, rivalId));

        verify(rivalRepositoryPort).saveAll(anyList());
        verify(userNoticeRepositoryPort).deleteApplyRivalNoticeByRivalId(pendingRival.id());
        verify(userNoticeRepositoryPort).saveAll(anyList());
    }

    @Test
    @DisplayName("수락 후 자동 취소된 PENDING의 상대방도 소켓 알림 대상에 포함된다")
    void execute_notifiesCancelledPendingOpponentsViaSocket() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        Long pendingOpponentId = 3L;

        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, opponentId, actor.id());
        Rival pendingRival = new Rival(20L, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, actor.id(), pendingOpponentId);

        when(acceptRivalPolicy.check(actor, rivalId)).thenReturn(rival);
        when(rivalRepositoryPort.save(any(Rival.class))).thenReturn(rival.accept());
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(inv -> inv.getArgument(0));
        when(rivalRepositoryPort.countAcceptedByUserId(actor.id())).thenReturn(4);
        when(rivalRepositoryPort.findAllPendingByUserId(actor.id())).thenReturn(List.of(pendingRival));
        when(rivalRepositoryPort.countAcceptedByUserId(opponentId)).thenReturn(2);

        acceptRivalService.execute(ModifyRivalData.Command.of(actor, rivalId));

        ArgumentCaptor<Collection<Long>> competeCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(competeRefetchNotifier).notifyCompeteChanged(competeCaptor.capture());
        assertThat(competeCaptor.getValue()).contains(opponentId, actor.id(), pendingOpponentId);
    }
}