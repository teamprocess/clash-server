package com.process.clash.adapter.persistence.github;

import com.process.clash.application.github.port.out.GithubDailyStatsQueryPort;
import com.process.clash.domain.github.entity.GithubDailyStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GithubDailyStatsQueryJpaAdapter implements GithubDailyStatsQueryPort {

    private final GithubDailyStatsJpaRepository repository;
    private final GithubDailyStatsJpaMapper mapper;

    @Override
    public Optional<GithubDailyStats> findByUserIdAndStudyDate(Long userId, LocalDate studyDate) {
        return repository.findByUserIdAndStudyDate(userId, studyDate)
                .map(mapper::toDomain);
    }

    @Override
    public List<Object[]> findDailyContributionsByUserIds(List<Long> userIds, LocalDate startDate, LocalDate endDate) {

        return repository.findDailyContributionsByUserIds(userIds, startDate, endDate);
    }

    @Override
    public List<Object[]> findWeeklyContributionsByUserIds(List<Long> userIds, LocalDate startDate, LocalDate endDate) {

        return repository.findWeeklyContributionsByUserIds(userIds, startDate, endDate);
    }

    @Override
    public List<Object[]> findMonthlyContributionsByUserIds(List<Long> userIds, LocalDate startDate, LocalDate endDate) {

        return repository.findMonthlyContributionsByUserIds(userIds, startDate, endDate);
    }

    @Override
    public double findAverageCommitsByUserIdAndPeriod(Long userId, LocalDate startDate, LocalDate endDate) {

        return repository.findAverageCommitsByUserIdAndPeriod(userId, startDate, endDate);
    }
}
