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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

    @Test
    @DisplayName("라이벌 수락 시 상대방과 본인 모두에게 알림 소켓 이벤트를 전송한다")
    void execute_notifiesBothUsersOnAccept() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, opponentId, actor.id());

        when(acceptRivalPolicy.check(actor, rivalId)).thenReturn(rival);
        when(rivalRepositoryPort.save(any(Rival.class))).thenReturn(rival.accept());
        when(rivalRepositoryPort.findOpponentIdByIdAndUserId(rivalId, actor.id())).thenReturn(opponentId);
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(inv -> inv.getArgument(0));

        acceptRivalService.execute(ModifyRivalData.Command.of(actor, rivalId));

        ArgumentCaptor<Collection<Long>> captor = ArgumentCaptor.forClass(Collection.class);
        verify(competeRefetchNotifier).notifyUserNoticeChanged(captor.capture());
        assertThat(captor.getValue()).containsExactlyInAnyOrder(opponentId, actor.id());

        ArgumentCaptor<Collection<Long>> competeCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(competeRefetchNotifier).notifyCompeteChanged(competeCaptor.capture());
        assertThat(competeCaptor.getValue()).containsExactlyInAnyOrder(opponentId, actor.id());
    }

    @Test
    @DisplayName("라이벌 수락 시 상대방과 본인 각각 알림을 저장한다")
    void execute_savesTwoNoticesOnAccept() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, opponentId, actor.id());

        when(acceptRivalPolicy.check(actor, rivalId)).thenReturn(rival);
        when(rivalRepositoryPort.save(any(Rival.class))).thenReturn(rival.accept());
        when(rivalRepositoryPort.findOpponentIdByIdAndUserId(rivalId, actor.id())).thenReturn(opponentId);
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(inv -> inv.getArgument(0));

        acceptRivalService.execute(ModifyRivalData.Command.of(actor, rivalId));

        verify(userNoticeRepositoryPort, times(2)).save(any(UserNotice.class));
    }
}
