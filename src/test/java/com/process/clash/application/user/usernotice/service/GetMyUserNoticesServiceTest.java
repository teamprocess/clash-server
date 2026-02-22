package com.process.clash.application.user.usernotice.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.user.usernotice.data.GetMyUserNoticesData;
import com.process.clash.application.user.usernotice.port.out.UserNoticeRepositoryPort;
import com.process.clash.domain.user.usernotice.entity.UserNotice;
import com.process.clash.domain.user.usernotice.enums.NoticeCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetMyUserNoticesServiceTest {

    @Mock
    private UserNoticeRepositoryPort userNoticeRepositoryPort;

    private GetMyUserNoticesService getMyUserNoticesService;

    @BeforeEach
    void setUp() {
        getMyUserNoticesService = new GetMyUserNoticesService(userNoticeRepositoryPort);
    }

    @Test
    @DisplayName("알림이 있을 때 최신순으로 목록을 반환한다")
    void execute_returnsNoticeList() {
        Actor actor = new Actor(1L);
        UserNotice notice1 = new UserNotice(
                1L, Instant.now(), Instant.now(),
                NoticeCategory.APPLY_RIVAL, false,
                2L, "senderA", "sender_a", null,
                1L, "receiverA", "receiver_a", null,
                null, null
        );
        UserNotice notice2 = new UserNotice(
                2L, Instant.now(), Instant.now(),
                NoticeCategory.ACCEPT_RIVAL, true,
                3L, "senderB", "sender_b", null,
                1L, "receiverA", "receiver_a", null,
                null, null
        );

        when(userNoticeRepositoryPort.findAllByReceiverId(actor.id())).thenReturn(List.of(notice1, notice2));

        GetMyUserNoticesData.Result result = getMyUserNoticesService.execute(GetMyUserNoticesData.Command.from(actor));

        assertThat(result.notices()).hasSize(2);
        assertThat(result.notices().get(0).id()).isEqualTo(1L);
        assertThat(result.notices().get(0).category()).isEqualTo(NoticeCategory.APPLY_RIVAL);
        assertThat(result.notices().get(0).isRead()).isFalse();
        assertThat(result.notices().get(0).senderId()).isEqualTo(2L);
        assertThat(result.notices().get(1).id()).isEqualTo(2L);
        assertThat(result.notices().get(1).category()).isEqualTo(NoticeCategory.ACCEPT_RIVAL);
        assertThat(result.notices().get(1).isRead()).isTrue();
    }

    @Test
    @DisplayName("알림이 없을 때 빈 목록을 반환한다")
    void execute_returnsEmptyListWhenNoNotices() {
        Actor actor = new Actor(1L);

        when(userNoticeRepositoryPort.findAllByReceiverId(actor.id())).thenReturn(List.of());

        GetMyUserNoticesData.Result result = getMyUserNoticesService.execute(GetMyUserNoticesData.Command.from(actor));

        assertThat(result.notices()).isEmpty();
    }
}
