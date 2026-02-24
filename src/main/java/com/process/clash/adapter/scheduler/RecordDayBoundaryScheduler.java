package com.process.clash.adapter.scheduler;

import com.process.clash.application.record.service.RecordDayBoundaryService;
import com.process.clash.application.record.v2.service.RecordDayBoundaryV2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecordDayBoundaryScheduler {

    private final RecordDayBoundaryService recordDayBoundaryService;
    private final RecordDayBoundaryV2Service recordDayBoundaryV2Service;

    @Scheduled(cron = "0 0 6 * * *", zone = "${record.timezone:Asia/Seoul}")
    public void rolloverActiveSessions() {
        log.info("Record day boundary rollover started.");
        safeRollover("scheduled");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void rolloverActiveSessionsOnStartup() {
        log.info("Record day boundary rollover on startup.");
        safeRollover("startup");
    }

    private void safeRollover(String trigger) {
        try {
            log.info("Record day boundary rollover started. trigger={}", trigger);
            rolloverV1AndV2();
        } catch (DataIntegrityViolationException exception) {
            log.error("Record day boundary rollover failed by data integrity violation. trigger={}", trigger, exception);
        } catch (Exception exception) {
            log.error("Record day boundary rollover failed. trigger={}", trigger, exception);
        }
    }

    private void rolloverV1AndV2() {
        recordDayBoundaryService.rolloverActiveSessionsAtBoundary();
        recordDayBoundaryV2Service.rolloverActiveSessionsAtBoundary();
        safeRollover("startup");
    }
}
