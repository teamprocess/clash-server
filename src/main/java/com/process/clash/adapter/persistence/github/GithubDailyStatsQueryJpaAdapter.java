package com.process.clash.adapter.persistence.github;

import com.process.clash.application.github.port.out.GithubDailyStatsQueryPort;
import com.process.clash.domain.github.entity.GithubDailyStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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
}
