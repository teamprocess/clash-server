package com.process.clash.adapter.scheduler;

import com.process.clash.application.github.service.GithubDailyStatsSyncService;
import com.process.clash.application.ranking.service.ZeroRankingDataInitService;
import com.process.clash.application.user.exp.service.GithubExpGrantService;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GithubStatsSyncScheduler {

    private final GithubDailyStatsSyncService syncService;
    private final GithubExpGrantService githubExpGrantService;
    private final ZeroRankingDataInitService zeroRankingDataInitService;
    private final AtomicBoolean syncRunning = new AtomicBoolean(false);

    // 6시에는 365일 동기화가 작동하기에 30일 동기화는 6시를 제외한 매 시간에 작동하도록 설정했습니다.
    @Async
    @Scheduled(cron = "0 0 0-5,7-23 * * *", zone = "${github.sync.timezone:Asia/Seoul}")
    public void runHourly30DaysSyncExceptMorningSix() {
        runExclusive("GitHub 30일 동기화", () -> syncAndGrant(syncService::syncRecent30Days));
    }

    // 365일 동기화는 매일 오전 6시에만 작동. (이 시각에는 30일 동기화가 중복되기에 작동하지 않음)
    @Async
    @Scheduled(cron = "0 0 6 * * *", zone = "${github.sync.timezone:Asia/Seoul}")
    public void runDaily365DaysSyncAtMorningSix() {
        if (!runExclusive("GitHub 365일 동기화", () -> syncAndGrant(syncService::syncRecent365Days))) {
            return;
        }
        try {
            zeroRankingDataInitService.initZeroExpForToday();
        } catch (Exception e) {
            log.error("0 EXP 초기화 실패.", e);
        }
    }

    private void syncAndGrant(Runnable syncAction) {
        syncAction.run();
        githubExpGrantService.grantForToday();
    }

    private boolean runExclusive(String jobName, Runnable action) {
        if (!syncRunning.compareAndSet(false, true)) {
            log.warn("{} 스케줄러를 건너뜁니다. 이전 GitHub 동기화가 아직 실행 중입니다.", jobName);
            return false;
        }

        long startedAt = System.currentTimeMillis();
        log.info("{} 스케줄러 시작.", jobName);
        try {
            action.run();
            log.info("{} 스케줄러 완료. elapsedMs={}", jobName, System.currentTimeMillis() - startedAt);
        } catch (Exception e) {
            log.error("{} 스케줄러 실패. elapsedMs={}", jobName, System.currentTimeMillis() - startedAt, e);
        } finally {
            syncRunning.set(false);
        }
        return true;
    }
}
