package com.process.clash.application.github.service;

import com.process.clash.application.github.exception.GithubRateLimitException;
import com.process.clash.application.github.model.GithubSyncTarget;
import com.process.clash.application.github.port.out.GithubDailyStatsStorePort;
import com.process.clash.application.github.port.out.GithubStatsFetchPort;
import com.process.clash.application.github.port.out.GithubSyncTargetPort;
import com.process.clash.domain.github.entity.GithubDailyStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GithubDailyStatsSyncService {

    private final GithubSyncTargetPort syncTargetPort;
    private final GithubStatsFetchPort statsFetchPort;
    private final GithubDailyStatsStorePort statsStorePort;
    private final StudyDateCalculator studyDateCalculator;
    private final Clock clock;

    @Value("${github.sync.recompute-days:30}")
    private int recomputeDays;

    @Value("${github.sync.default-token:}")
    private String defaultToken;

    public void syncRecentDays() {
        List<GithubSyncTarget> targets = syncTargetPort.findSyncTargets();
        if (targets.isEmpty()) {
            log.info("No GitHub sync targets found.");
            return;
        }

        Instant now = clock.instant();
        List<LocalDate> studyDates = studyDateCalculator.recentStudyDates(now, recomputeDays);
        if (studyDates.isEmpty()) {
            log.info("No study dates to recompute. recomputeDays={}", recomputeDays);
            return;
        }

        for (GithubSyncTarget target : targets) {
            String accessToken = resolveAccessToken(target);
            if (accessToken == null || accessToken.isBlank()) {
                log.warn("Skipping GitHub sync: missing access token. userId={}, login={}",
                        target.userId(), target.githubLogin());
                continue;
            }
            try {
                GithubSyncTarget resolvedTarget = new GithubSyncTarget(
                        target.userId(),
                        target.githubLogin(),
                        target.githubUserNodeId(),
                        target.emails(),
                        accessToken
                );
                List<GithubDailyStats> stats = statsFetchPort.fetchDailyStats(resolvedTarget, studyDates);
                if (!stats.isEmpty()) {
                    statsStorePort.upsertAll(stats);
                }
                log.info("GitHub daily stats sync completed. userId={}, days={}",
                        target.userId(), studyDates.size());
            } catch (GithubRateLimitException ex) {
                log.warn("GitHub rate limit hit. userId={}, resetAt={}",
                        target.userId(), ex.getResetAt());
            } catch (Exception ex) {
                log.error("GitHub daily stats sync failed. userId={}, login={}",
                        target.userId(), target.githubLogin(), ex);
            }
        }
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
}
