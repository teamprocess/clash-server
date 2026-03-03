package com.process.clash.application.github.port.out;

import com.process.clash.application.compete.my.data.Streak;
import com.process.clash.application.compete.my.data.Variation;
import com.process.clash.application.github.data.GitHubDailyContributionDto;
import com.process.clash.application.ranking.data.UserRanking;
import com.process.clash.domain.github.entity.GitHubDailyStats;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GitHubDailyStatsQueryPort {
    Optional<GitHubDailyStats> findByUserIdAndStudyDate(Long userId, LocalDate studyDate);
    List<GitHubDailyStats> findAllByStudyDate(LocalDate studyDate);

    List<GitHubDailyContributionDto> findDailyContributionsByUserId(Long userId, LocalDate startDate, LocalDate endDate, PageRequest pageRequest);
    List<Object[]> findDailyContributionsByUserIds(List<Long> userIds, LocalDate startDate, LocalDate endDate);
    List<Object[]> findWeeklyContributionsByUserIds(List<Long> userIds, LocalDate startDate, LocalDate endDate);
    List<Object[]> findMonthlyContributionsByUserIds(List<Long> userIds, LocalDate startDate, LocalDate endDate);

    double findTotalCommitCountByUserIdAndPeriod(Long userId, LocalDate startDate, LocalDate endDate);
    double findAverageContributionByUserIdAndPeriod(Long userId, LocalDate startDate, LocalDate endDate);
    List<Streak> findStreakByUserId(Long userId, LocalDate startDate, LocalDate endDate);
    List<Variation> findVaricationByUserId(Long userId, LocalDate startDate, LocalDate endDate);

    List<UserRanking> findGitHubRankingByUserIdAndPeriod(Long userId, LocalDate startDate, LocalDate endDate);
}
