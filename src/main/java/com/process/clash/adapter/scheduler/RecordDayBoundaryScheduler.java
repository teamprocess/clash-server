package com.process.clash.adapter.scheduler;

import com.process.clash.application.record.service.RecordDayBoundaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecordDayBoundaryScheduler {

    private final RecordDayBoundaryService recordDayBoundaryService;

    @Scheduled(cron = "0 0 6 * * *", zone = "${record.timezone:Asia/Seoul}")
    public void rolloverActiveSessions() {
        log.info("Record day boundary rollover started.");
        recordDayBoundaryService.rolloverActiveSessionsAtBoundary();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void rolloverActiveSessionsOnStartup() {
        log.info("Record day boundary rollover on startup.");
        recordDayBoundaryService.rolloverActiveSessionsAtBoundary();
    }
}
