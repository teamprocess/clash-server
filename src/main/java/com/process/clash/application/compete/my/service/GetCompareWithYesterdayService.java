package com.process.clash.application.compete.my.service;

import com.process.clash.application.compete.my.data.GetCompareWithYesterdayData;
import com.process.clash.application.compete.my.port.in.GetCompareWithYesterdayUseCase;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.application.user.userstudytime.exception.exception.notfound.UserStudyTimeNotFoundException;
import com.process.clash.application.user.userstudytime.port.out.UserStudyTimeRepositoryPort;
import com.process.clash.application.github.exception.exception.notfound.GithubDailyStatsNotFoundException;
import com.process.clash.application.github.port.out.GithubDailyStatsQueryPort;
import com.process.clash.domain.github.entity.GithubDailyStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetCompareWithYesterdayService implements GetCompareWithYesterdayUseCase {

    private final StudySessionRepositoryPort studySessionRepositoryPort;
    private final UserStudyTimeRepositoryPort userStudyTimeRepositoryPort;
    private final GithubDailyStatsQueryPort githubDailyStatsQueryPort;

    @Override
    public GetCompareWithYesterdayData.Result execute(GetCompareWithYesterdayData.Command command) {

        LocalDate today = LocalDate.now();

        LocalDate yesterday = today.minusDays(1);

        Long yesterdayActiveTime =
                userStudyTimeRepositoryPort.findByUserIdAndDate(command.actor().id(), yesterday)
                        .orElseThrow(UserStudyTimeNotFoundException::new)
                        .totalStudyTimeSeconds();

        LocalDateTime startOfDay = today.atTime(6, 0, 0);
        LocalDateTime endOfDay = today.plusDays(1).atTime(6, 0, 0);

        Long todayActiveTime = studySessionRepositoryPort.getTotalStudyTimeInSeconds(
                command.actor().id(),
                startOfDay,
                endOfDay
        );

        GithubDailyStats yesterdayStats = githubDailyStatsQueryPort
                .findByUserIdAndStudyDate(command.actor().id(), yesterday)
                .orElseThrow(GithubDailyStatsNotFoundException::new);
        GithubDailyStats todayStats = githubDailyStatsQueryPort
                .findByUserIdAndStudyDate(command.actor().id(), today)
                .orElseThrow(GithubDailyStatsNotFoundException::new);

        Integer yesterdayContributions = toContributionCount(yesterdayStats);
        Integer todayContributions = toContributionCount(todayStats);

        return GetCompareWithYesterdayData.Result.from(yesterdayActiveTime, todayActiveTime, yesterdayContributions, todayContributions);
    }

    private int toContributionCount(GithubDailyStats stats) {
        return stats.commitCount()
                + stats.prCount()
                + stats.issueCount()
                + stats.reviewedPrCount();
    }
}
