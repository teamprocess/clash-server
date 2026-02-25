package com.process.clash.application.compete.rival.rival.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.realtime.CompeteRefetchNotifier;
import com.process.clash.application.compete.rival.rival.data.ApplyRivalData;
import com.process.clash.application.compete.rival.rival.policy.ApplyRivalPolicy;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.usernotice.port.out.UserNoticeRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ApplyRivalServiceTest {

    @Mock
    private ApplyRivalPolicy applyRivalPolicy;

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    @Mock
    private UserNoticeRepositoryPort userNoticeRepositoryPort;

    @Mock
    private CompeteRefetchNotifier competeRefetchNotifier;

    private ApplyRivalService applyRivalService;

    @BeforeEach
    void setUp() {
        applyRivalService = new ApplyRivalService(
                applyRivalPolicy,
                rivalRepositoryPort,
                userNoticeRepositoryPort,
                competeRefetchNotifier
        );
    }

    @Test
    @DisplayName("라이벌 신청 시 신청 받은 상대방들에게 알림 소켓 이벤트를 전송한다")
    void execute_notifiesOpponentsOnApply() {
        Actor actor = new Actor(1L);
        ApplyRivalData.Command command = new ApplyRivalData.Command(
                actor,
                List.of(new ApplyRivalData.Id(2L), new ApplyRivalData.Id(3L))
        );

        applyRivalService.execute(command);

        ArgumentCaptor<Collection<Long>> captor = ArgumentCaptor.forClass(Collection.class);
        verify(competeRefetchNotifier).notifyUserNoticeChanged(captor.capture());
        assertThat(captor.getValue()).containsExactlyInAnyOrder(2L, 3L);

        ArgumentCaptor<Collection<Long>> competeCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(competeRefetchNotifier).notifyCompeteChanged(competeCaptor.capture());
        assertThat(competeCaptor.getValue()).containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    @DisplayName("라이벌 신청 시 라이벌 데이터와 알림을 저장한다")
    void execute_savesRivalsAndNotices() {
        Actor actor = new Actor(1L);
        ApplyRivalData.Command command = new ApplyRivalData.Command(
                actor,
                List.of(new ApplyRivalData.Id(2L))
        );

        applyRivalService.execute(command);

        verify(rivalRepositoryPort).saveAll(org.mockito.ArgumentMatchers.anyList());
        verify(userNoticeRepositoryPort).saveAll(org.mockito.ArgumentMatchers.anyList());
    }
}
