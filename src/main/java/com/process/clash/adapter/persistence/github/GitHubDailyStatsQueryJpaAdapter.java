package com.process.clash.adapter.persistence.github;

import com.process.clash.application.compete.my.data.Streak;
import com.process.clash.application.compete.my.data.Variation;
import com.process.clash.application.github.port.out.GitHubDailyStatsQueryPort;
import com.process.clash.domain.github.entity.GitHubDailyStats;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GitHubDailyStatsQueryJpaAdapter implements GitHubDailyStatsQueryPort {

    private final GitHubDailyStatsJpaRepository repository;
    private final GitHubDailyStatsJpaMapper mapper;

    @Override
    public Optional<GitHubDailyStats> findByUserIdAndStudyDate(Long userId, LocalDate studyDate) {
        return repository.findByUserIdAndStudyDate(userId, studyDate)
                .map(mapper::toDomain);
    }

    @Override
    public List<Object[]> findDailyContributionsByUserIds(List<Long> userIds, LocalDate startDate, LocalDate endDate, PageRequest pageRequest) {

        return repository.findDailyContributionsByUserIds(userIds, startDate, endDate, pageRequest);
    }

    @Override
    public List<Object[]> findWeeklyContributionsByUserIds(List<Long> userIds, LocalDate startDate, LocalDate endDate, PageRequest pageRequest) {

        return repository.findWeeklyContributionsByUserIds(userIds, startDate, endDate, pageRequest);
    }

    @Override
    public List<Object[]> findMonthlyContributionsByUserIds(List<Long> userIds, LocalDate startDate, LocalDate endDate, PageRequest pageRequest) {

        return repository.findMonthlyContributionsByUserIds(userIds, startDate, endDate, pageRequest);
    }

    @Override
    public double findAverageContributionByUserIdAndPeriod(Long userId, LocalDate startDate, LocalDate endDate) {

        return repository.findAverageContributionByUserIdAndPeriod(userId, startDate, endDate);
    }

    @Override
    public List<Streak> findStreakByUserId(Long userId, LocalDate startDate, LocalDate endDate) {

        return repository.findStreakByUserId(userId, startDate, endDate);
    }

    @Override
    public List<Variation> findVaricationByUserId(Long userId, LocalDate startDate, LocalDate endDate) {

        return repository.findVariationByUserId(userId, startDate, endDate);
    }
}
