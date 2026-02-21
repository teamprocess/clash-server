package com.process.clash.application.compete.rival.rival.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.realtime.CompeteRefetchNotifier;
import com.process.clash.application.compete.rival.rival.data.ModifyRivalData;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RejectRivalServiceTest {

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    @Mock
    private UserNoticeRepositoryPort userNoticeRepositoryPort;

    @Mock
    private CompeteRefetchNotifier competeRefetchNotifier;

    private RejectRivalService rejectRivalService;

    @BeforeEach
    void setUp() {
        rejectRivalService = new RejectRivalService(
                rivalRepositoryPort,
                userNoticeRepositoryPort,
                competeRefetchNotifier
        );
    }

    @Test
    @DisplayName("라이벌 거절 시 상대방과 본인 모두에게 알림 소켓 이벤트를 전송한다")
    void execute_notifiesBothUsersOnReject() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, opponentId, actor.id());

        when(rivalRepositoryPort.findById(rivalId)).thenReturn(Optional.of(rival));
        when(rivalRepositoryPort.save(any(Rival.class))).thenReturn(rival.reject());
        when(rivalRepositoryPort.findOpponentIdByIdAndUserIdInRejectCase(rivalId, actor.id())).thenReturn(opponentId);
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(inv -> inv.getArgument(0));

        rejectRivalService.execute(ModifyRivalData.Command.of(actor, rivalId));

        ArgumentCaptor<Collection<Long>> captor = ArgumentCaptor.forClass(Collection.class);
        verify(competeRefetchNotifier).notifyUserNoticeChanged(captor.capture());
        assertThat(captor.getValue()).containsExactlyInAnyOrder(opponentId, actor.id());
    }

    @Test
    @DisplayName("라이벌 거절 시 상대방과 본인 각각 알림을 저장한다")
    void execute_savesTwoNoticesOnReject() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, opponentId, actor.id());

        when(rivalRepositoryPort.findById(rivalId)).thenReturn(Optional.of(rival));
        when(rivalRepositoryPort.save(any(Rival.class))).thenReturn(rival.reject());
        when(rivalRepositoryPort.findOpponentIdByIdAndUserIdInRejectCase(rivalId, actor.id())).thenReturn(opponentId);
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(inv -> inv.getArgument(0));

        rejectRivalService.execute(ModifyRivalData.Command.of(actor, rivalId));

        verify(userNoticeRepositoryPort, times(2)).save(any(UserNotice.class));
    }

    @Test
    @DisplayName("존재하지 않는 라이벌 ID로 거절하면 RivalNotFoundException이 발생한다")
    void execute_throwsWhenRivalNotFound() {
        Actor actor = new Actor(1L);
        Long rivalId = 99L;

        when(rivalRepositoryPort.findById(rivalId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rejectRivalService.execute(ModifyRivalData.Command.of(actor, rivalId)))
                .isInstanceOf(RivalNotFoundException.class);

        verify(userNoticeRepositoryPort, never()).save(any());
        verify(competeRefetchNotifier, never()).notifyUserNoticeChanged(any());
    }
}
