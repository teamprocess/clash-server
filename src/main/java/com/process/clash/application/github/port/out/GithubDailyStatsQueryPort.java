package com.process.clash.application.github.port.out;

import com.process.clash.domain.github.entity.GithubDailyStats;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GithubDailyStatsQueryPort {
    Optional<GithubDailyStats> findByUserIdAndStudyDate(Long userId, LocalDate studyDate);

    List<Object[]> findDailyContributionsByUserIds(List<Long> userIds, LocalDate startDate, LocalDate endDate);
    List<Object[]> findWeeklyContributionsByUserIds(List<Long> userIds, LocalDate startDate, LocalDate endDate);
    List<Object[]> findMonthlyContributionsByUserIds(List<Long> userIds, LocalDate startDate, LocalDate endDate);

    double findAverageCommitsByUserIdAndPeriod(Long userId, LocalDate startDate, LocalDate endDate);
}
