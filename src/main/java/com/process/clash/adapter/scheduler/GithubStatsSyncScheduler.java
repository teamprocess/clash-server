package com.process.clash.adapter.scheduler;

import com.process.clash.application.github.service.GithubDailyStatsSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GithubStatsSyncScheduler {

    private final GithubDailyStatsSyncService syncService;

    @Scheduled(cron = "0 0 * * * *", zone = "${github.sync.timezone:Asia/Seoul}")
    public void runHourlySync() {
        log.info("GitHub 일일 통계 동기화 스케줄러 시작.");
        syncService.syncRecentDays();
    }
}
