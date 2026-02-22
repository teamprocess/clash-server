package com.process.clash.application.user.usernotice.service;

import com.process.clash.application.user.usernotice.data.ReadUserNoticeData;
import com.process.clash.application.user.usernotice.exception.exception.notfound.UserNoticeNotFoundException;
import com.process.clash.application.user.usernotice.port.in.ReadUserNoticeUseCase;
import com.process.clash.application.user.usernotice.port.out.UserNoticeRepositoryPort;
import com.process.clash.domain.user.usernotice.entity.UserNotice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReadUserNoticeService implements ReadUserNoticeUseCase {

    private final UserNoticeRepositoryPort userNoticeRepositoryPort;

    @Override
    public void execute(ReadUserNoticeData.Command command) {
        UserNotice notice = userNoticeRepositoryPort
                .findByIdAndReceiverId(command.noticeId(), command.actor().id())
                .orElseThrow(UserNoticeNotFoundException::new);

        if (notice.isRead()) {
            return;
        }

        userNoticeRepositoryPort.save(notice.markAsRead());
    }
}
