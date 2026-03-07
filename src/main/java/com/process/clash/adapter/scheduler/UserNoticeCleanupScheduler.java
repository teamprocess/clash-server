package com.process.clash.adapter.scheduler;

import com.process.clash.application.user.usernotice.service.UserNoticeCleanupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserNoticeCleanupScheduler {

    private final UserNoticeCleanupService userNoticeCleanupService;

    @Scheduled(cron = "0 0 0 1,15 * *", zone = "${app.timezone:Asia/Seoul}")
    public void cleanupAllNotices() {
        safeCleanup("scheduled");
    }

    private void safeCleanup(String trigger) {
        try {
            log.info("User notice cleanup started. trigger={}", trigger);
            userNoticeCleanupService.deleteAllNotices();
            log.info("User notice cleanup completed. trigger={}", trigger);
        } catch (Exception exception) {
            log.error("User notice cleanup failed. trigger={}", trigger, exception);
        }
    }
}
