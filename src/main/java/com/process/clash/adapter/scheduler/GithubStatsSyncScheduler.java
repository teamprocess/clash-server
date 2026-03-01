package com.process.clash.adapter.scheduler;

import com.process.clash.application.github.service.GithubDailyStatsSyncService;
import com.process.clash.application.user.exp.service.GithubExpGrantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GithubStatsSyncScheduler {

    private final GithubDailyStatsSyncService syncService;
    private final GithubExpGrantService githubExpGrantService;

    // 6시에는 365일 동기화가 작동하기에 30일 동기화는 6시를 제외한 매 시간에 작동하도록 설정했습니다.
    @Scheduled(cron = "0 0 0-5,7-23 * * *", zone = "${github.sync.timezone:Asia/Seoul}")
    public void runHourly30DaysSyncExceptMorningSix() {
        log.info("GitHub 30일 동기화 스케줄러 시작.");
        syncService.syncRecent30Days();
        githubExpGrantService.grantForToday();
    }

    // 365일 동기화는 매일 오전 6시에만 작동. (이 시각에는 30일 동기화가 중복되기에 작동하지 않음)
    @Scheduled(cron = "0 0 6 * * *", zone = "${github.sync.timezone:Asia/Seoul}")
    public void runDaily365DaysSyncAtMorningSix() {
        log.info("GitHub 365일 동기화 스케줄러 시작.");
        syncService.syncRecent365Days();
        githubExpGrantService.grantForToday();
    }
}
