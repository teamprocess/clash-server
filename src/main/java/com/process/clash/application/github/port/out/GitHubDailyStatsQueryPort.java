package com.process.clash.application.github.port.out;

import com.process.clash.application.compete.my.data.Streak;
import com.process.clash.application.compete.my.data.Variation;
import com.process.clash.domain.github.entity.GitHubDailyStats;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GitHubDailyStatsQueryPort {
    Optional<GitHubDailyStats> findByUserIdAndStudyDate(Long userId, LocalDate studyDate);

    List<Object[]> findDailyContributionsByUserIds(List<Long> userIds, LocalDate startDate, LocalDate endDate);
    List<Object[]> findWeeklyContributionsByUserIds(List<Long> userIds, LocalDate startDate, LocalDate endDate);
    List<Object[]> findMonthlyContributionsByUserIds(List<Long> userIds, LocalDate startDate, LocalDate endDate);

    double findAverageContributionByUserIdAndPeriod(Long userId, LocalDate startDate, LocalDate endDate);
    List<Streak> findStreakByUserId(Long userId, LocalDate standard);
    List<Variation> findVaricationByUserId(Long userId, LocalDate standard);
}
