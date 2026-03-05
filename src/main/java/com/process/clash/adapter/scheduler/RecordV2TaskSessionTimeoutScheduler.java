package com.process.clash.adapter.scheduler;

import com.process.clash.application.record.v2.service.RecordV2TaskSessionTimeoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecordV2TaskSessionTimeoutScheduler {

    private final RecordV2TaskSessionTimeoutService recordV2TaskSessionTimeoutService;

    @Scheduled(cron = "0 * * * * *", zone = "${record.timezone:Asia/Seoul}")
    public void stopExpiredTaskSessions() {
        safeStopExpiredTaskSessions("scheduled");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void stopExpiredTaskSessionsOnStartup() {
        safeStopExpiredTaskSessions("startup");
    }

    private void safeStopExpiredTaskSessions(String trigger) {
        try {
            int stoppedCount = recordV2TaskSessionTimeoutService.stopExpiredTaskSessions();
            if (stoppedCount > 0) {
                log.info("Record V2 task session timeout applied. trigger={}, stoppedCount={}", trigger, stoppedCount);
            }
        } catch (Exception exception) {
            log.error("Record V2 task session timeout failed. trigger={}", trigger, exception);
        }
    }
}
