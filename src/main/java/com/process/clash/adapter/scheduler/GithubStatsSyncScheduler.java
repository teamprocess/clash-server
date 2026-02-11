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

    @Scheduled(cron = "0 0 0-5,7-23 * * *", zone = "${github.sync.timezone:Asia/Seoul}")
    public void runHourly30DaysSyncExceptMorningSix() {
        log.info("GitHub 30일 동기화 스케줄러 시작.");
        syncService.syncRecent30Days();
    }

    @Scheduled(cron = "0 0 6 * * *", zone = "${github.sync.timezone:Asia/Seoul}")
    public void runDaily365DaysSyncAtMorningSix() {
        log.info("GitHub 365일 동기화 스케줄러 시작.");
        syncService.syncRecent365Days();
    }
}
