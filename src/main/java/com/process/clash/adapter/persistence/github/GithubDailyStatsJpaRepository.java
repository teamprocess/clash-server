package com.process.clash.adapter.persistence.github;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GithubDailyStatsJpaRepository extends JpaRepository<GithubDailyStatsJpaEntity, Long> {
    List<GithubDailyStatsJpaEntity> findByUserIdAndStudyDateIn(Long userId, List<LocalDate> studyDates);

    Optional<GithubDailyStatsJpaEntity> findByUserIdAndStudyDate(Long userId, LocalDate studyDate);
}
