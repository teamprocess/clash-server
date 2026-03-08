package com.process.clash.application.user.usernotice.service;

import com.process.clash.application.user.usernotice.port.out.UserNoticeRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserNoticeCleanupService {

    private final UserNoticeRepositoryPort userNoticeRepositoryPort;

    @Transactional
    public void deleteAllNotices() {
        userNoticeRepositoryPort.deleteAllNotices();
    }
}
