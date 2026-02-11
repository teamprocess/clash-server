package com.process.clash.application.github.service;

import com.process.clash.application.github.exception.GithubRateLimitException;
import com.process.clash.application.github.model.GithubSyncTarget;
import com.process.clash.application.github.port.out.GithubDailyStatsStorePort;
import com.process.clash.application.github.port.out.GithubStatsFetchPort;
import com.process.clash.application.github.port.out.GithubSyncTargetPort;
import com.process.clash.domain.github.entity.GitHubDailyStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
@Slf4j
public class GithubDailyStatsSyncService {
    private static final int HOURLY_SYNC_DAYS = 30;
    private static final int DAILY_MORNING_SYNC_DAYS = 365;


    private final GithubSyncTargetPort syncTargetPort;
    private final GithubStatsFetchPort statsFetchPort;
    private final GithubDailyStatsStorePort statsStorePort;
    private final StudyDateCalculator studyDateCalculator;
    private final Clock clock;
    private final ExecutorService githubSyncExecutor;

    @Value("${github.sync.recompute-days:30}")
    private int recomputeDays;

    @Value("${github.sync.default-token:}")
    private String defaultToken;

    @Value("${github.sync.max-concurrency:1}")
    private int maxConcurrency;

    public void syncRecentDays() {
        syncRecentDays(recomputeDays);
    }

    public void syncRecent30Days() {
        syncRecentDays(HOURLY_SYNC_DAYS);
    }

    public void syncRecent365Days() {
        syncRecentDays(DAILY_MORNING_SYNC_DAYS);
    }

    private void syncRecentDays(int daysToRecompute) {
        List<GithubSyncTarget> targets = syncTargetPort.findSyncTargets();
        if (targets.isEmpty()) {
            log.info("GitHub 동기화 대상이 없습니다.");
            return;
        }

        // 최근 N일의 "학습일" 기준으로 동기화 범위를 계산
        Instant now = clock.instant();
        List<LocalDate> studyDates = studyDateCalculator.recentStudyDates(now, daysToRecompute);
        if (studyDates.isEmpty()) {
            log.info("재계산할 학습일이 없습니다. recomputeDays={}", daysToRecompute);
            return;
        }

        // max-concurrency 설정에 따라 순차/병렬 처리 분기
        if (maxConcurrency <= 1) {
            for (GithubSyncTarget target : targets) {
                syncTarget(target, studyDates);
            }
            return;
        }

        // 사용자별 동기화를 제한된 스레드풀에서 병렬 실행
        List<CompletableFuture<Void>> futures = targets.stream()
                .map(target -> CompletableFuture.runAsync(
                        () -> syncTarget(target, studyDates),
                        githubSyncExecutor
                ))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private String resolveAccessToken(GithubSyncTarget target) {
        if (target.accessToken() != null && !target.accessToken().isBlank()) {
            return target.accessToken();
        }
        if (defaultToken != null && !defaultToken.isBlank()) {
            return defaultToken;
        }
        return null;
    }

    private void syncTarget(GithubSyncTarget target, List<LocalDate> studyDates) {
        String accessToken = resolveAccessToken(target);
        if (accessToken == null || accessToken.isBlank()) {
            log.warn("GitHub 동기화를 건너뜁니다. 액세스 토큰이 없습니다. userId={}, login={}",
                    target.userId(), target.githubLogin());
            return;
        }
        try {
            // 기본 토큰 대체 및 사용자 정보 정규화
            GithubSyncTarget resolvedTarget = new GithubSyncTarget(
                    target.userId(),
                    target.githubLogin(),
                    target.githubUserNodeId(),
                    target.emails(),
                    accessToken
            );
            List<GitHubDailyStats> stats = statsFetchPort.fetchDailyStats(resolvedTarget, studyDates);
            if (!stats.isEmpty()) {
                statsStorePort.upsertAll(stats);
            }
            log.info("GitHub 통계 동기화 완료. userId={}, days={}",
                    target.userId(), studyDates.size());
        } catch (GithubRateLimitException ex) {
            log.warn("GitHub 요청 제한에 도달했습니다. userId={}, resetAt={}",
                    target.userId(), ex.getResetAt());
        } catch (Exception ex) {
            log.error("GitHub 통계 동기화 실패. userId={}, login={}",
                    target.userId(), target.githubLogin(), ex);
        }
    }
}
