package com.process.clash.application.user.usernotice.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.user.usernotice.data.ReadUserNoticeData;
import com.process.clash.application.user.usernotice.exception.exception.notfound.UserNoticeNotFoundException;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReadUserNoticeServiceTest {

    @Mock
    private UserNoticeRepositoryPort userNoticeRepositoryPort;

    private ReadUserNoticeService readUserNoticeService;

    @BeforeEach
    void setUp() {
        readUserNoticeService = new ReadUserNoticeService(userNoticeRepositoryPort);
    }

    @Test
    @DisplayName("읽지 않은 알림을 읽음 처리하면 save()가 호출된다")
    void execute_savesWhenNoticeIsUnread() {
        Actor actor = new Actor(1L);
        Long noticeId = 10L;
        UserNotice unreadNotice = new UserNotice(noticeId, Instant.now(), Instant.now(), NoticeCategory.APPLY_RIVAL, false, 2L, 1L);

        when(userNoticeRepositoryPort.findByIdAndReceiverId(noticeId, actor.id())).thenReturn(Optional.of(unreadNotice));
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenReturn(unreadNotice.markAsRead());

        readUserNoticeService.execute(ReadUserNoticeData.Command.of(actor, noticeId));

        verify(userNoticeRepositoryPort).save(unreadNotice.markAsRead());
    }

    @Test
    @DisplayName("이미 읽은 알림은 save()를 호출하지 않는다")
    void execute_skipsSaveWhenNoticeIsAlreadyRead() {
        Actor actor = new Actor(1L);
        Long noticeId = 10L;
        UserNotice readNotice = new UserNotice(noticeId, Instant.now(), Instant.now(), NoticeCategory.APPLY_RIVAL, true, 2L, 1L);

        when(userNoticeRepositoryPort.findByIdAndReceiverId(noticeId, actor.id())).thenReturn(Optional.of(readNotice));

        readUserNoticeService.execute(ReadUserNoticeData.Command.of(actor, noticeId));

        verify(userNoticeRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("존재하지 않는 알림 ID로 읽음 처리하면 UserNoticeNotFoundException이 발생한다")
    void execute_throwsWhenNoticeNotFound() {
        Actor actor = new Actor(1L);
        Long noticeId = 99L;

        when(userNoticeRepositoryPort.findByIdAndReceiverId(noticeId, actor.id())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> readUserNoticeService.execute(ReadUserNoticeData.Command.of(actor, noticeId)))
                .isInstanceOf(UserNoticeNotFoundException.class);

        verify(userNoticeRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("타인의 알림 ID로 읽음 처리하면 UserNoticeNotFoundException이 발생한다")
    void execute_throwsWhenNoticeDoesNotBelongToActor() {
        Actor actor = new Actor(1L);
        Long noticeId = 10L;

        // receiverId가 1L이 아닌 알림은 findByIdAndReceiverId에서 empty를 반환한다
        when(userNoticeRepositoryPort.findByIdAndReceiverId(noticeId, actor.id())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> readUserNoticeService.execute(ReadUserNoticeData.Command.of(actor, noticeId)))
                .isInstanceOf(UserNoticeNotFoundException.class);
    }
}
