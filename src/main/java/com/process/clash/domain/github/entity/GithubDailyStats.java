package com.process.clash.domain.github.entity;

import java.time.Instant;
import java.time.LocalDate;

public record GithubDailyStats(
        Long userId,
        LocalDate studyDate,
        int commitCount,
        int prCount,
        int issueCount,
        int reviewedPrCount,
        long additions,
        long deletions,
        Instant syncedAt
) {

    public int getTotalContributionCount() {

        return this.commitCount + this.prCount + this.reviewedPrCount;
    }
}
