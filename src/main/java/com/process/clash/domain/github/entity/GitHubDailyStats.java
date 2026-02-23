package com.process.clash.domain.github.entity;

import java.time.Instant;
import java.time.LocalDate;

public record GitHubDailyStats(
        Long userId,
        LocalDate studyDate,
        int commitCount,
        int prCount,
        int issueCount,
        int reviewedPrCount,
        long additions,
        long deletions,
        String topCommitRepo,
        String topPrRepo,
        Instant firstCommitAt,
        Instant lastCommitAt,
        int prMergedCount,
        int prOpenCount,
        int prClosedCount,
        Instant syncedAt
) {

    public int getTotalContributionCount() {

        return this.commitCount + this.prCount + this.reviewedPrCount + this.issueCount;
    }

    // 예외 안나오게 기본 생성자로 껍데기만 만들기
    public GitHubDailyStats(Long userId, LocalDate studyDate) {
        this(userId, studyDate, 0, 0, 0, 0, 0L, 0L, null, null, null, null, 0, 0, 0, Instant.now());
    }
}
