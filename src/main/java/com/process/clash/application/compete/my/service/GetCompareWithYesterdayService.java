package com.process.clash.application.compete.my.service;

import com.process.clash.application.compete.my.data.GetCompareWithYesterdayData;
import com.process.clash.application.compete.my.port.in.GetCompareWithYesterdayUseCase;
import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import com.process.clash.application.user.userstudytime.exception.exception.notfound.UserStudyTimeNotFoundException;
import com.process.clash.application.user.userstudytime.port.out.UserStudyTimeRepositoryPort;
import com.process.clash.application.github.exception.exception.notfound.GithubDailyStatsNotFoundException;
import com.process.clash.application.github.port.out.GitHubDailyStatsQueryPort;
import com.process.clash.domain.github.entity.GitHubDailyStats;
import com.process.clash.domain.user.userstudytime.entity.UserStudyTime;
import com.process.clash.infrastructure.config.record.RecordProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetCompareWithYesterdayService implements GetCompareWithYesterdayUseCase {

    private final RecordSessionRepositoryPort recordSessionRepositoryPort;
    private final UserStudyTimeRepositoryPort userStudyTimeRepositoryPort;
    private final GitHubDailyStatsQueryPort githubDailyStatsQueryPort;
    private final RecordProperties recordProperties;
    private final ZoneId recordZoneId;

    @Override
    public GetCompareWithYesterdayData.Result execute(GetCompareWithYesterdayData.Command command) {

        ZonedDateTime nowZoned = ZonedDateTime.now(recordZoneId);
        int boundaryHour = recordProperties.dayBoundaryHour();
        LocalDate today;
        if (nowZoned.getHour() < boundaryHour) {
            today = nowZoned.toLocalDate().minusDays(1);
        } else {
            today = nowZoned.toLocalDate();
        }

        LocalDate yesterday = today.minusDays(1);

        Long yesterdayActiveTime =
                userStudyTimeRepositoryPort.findByUserIdAndDate(command.actor().id(), yesterday)
                        .map(UserStudyTime::totalStudyTimeSeconds)
                        .orElse(0L);

        LocalDateTime startOfDay = today.atTime(boundaryHour, 0, 0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        Long todayActiveTime = recordSessionRepositoryPort.getTotalStudyTimeInSeconds(
                command.actor().id(),
                startOfDay,
                endOfDay
        );

        GitHubDailyStats yesterdayStats = githubDailyStatsQueryPort
                .findByUserIdAndStudyDate(command.actor().id(), yesterday)
                .orElseGet(() -> new GitHubDailyStats(command.actor().id(), yesterday));
        GitHubDailyStats todayStats = githubDailyStatsQueryPort
                .findByUserIdAndStudyDate(command.actor().id(), today)
                .orElseGet(() -> new GitHubDailyStats(command.actor().id(), today));

        Integer yesterdayContributions = toContributionCount(yesterdayStats);
        Integer todayContributions = toContributionCount(todayStats);

        return GetCompareWithYesterdayData.Result.of(yesterdayActiveTime, todayActiveTime, yesterdayContributions, todayContributions);
    }

    private int toContributionCount(GitHubDailyStats stats) {
        return stats.commitCount()
                + stats.prCount()
                + stats.issueCount()
                + stats.reviewedPrCount();
    }
}
