package com.process.clash.application.github.port.out;

import com.process.clash.domain.github.entity.GithubDailyStats;

import java.time.LocalDate;
import java.util.Optional;

public interface GithubDailyStatsQueryPort {
    Optional<GithubDailyStats> findByUserIdAndStudyDate(Long userId, LocalDate studyDate);
}
