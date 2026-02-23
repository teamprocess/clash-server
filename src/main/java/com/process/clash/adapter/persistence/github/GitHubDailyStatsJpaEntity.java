package com.process.clash.adapter.persistence.github;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(
        name = "github_daily_stats",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_github_daily_stats_user_date",
                columnNames = {"user_id", "study_date"}
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@IdClass(GitHubDailyStatsId.class)
public class GitHubDailyStatsJpaEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(nullable = false)
    private LocalDate studyDate;

    @Column(nullable = false)
    private int commitCount;

    @Column(nullable = false)
    private int prCount;

    @Column(nullable = false)
    private int issueCount;

    @Column(nullable = false)
    private int reviewCount;

    @Column(nullable = false)
    private long additions;

    @Column(nullable = false)
    private long deletions;

    @Column
    private String topCommitRepo;

    @Column
    private String topPrRepo;

    @Column
    private Instant firstCommitAt;

    @Column
    private Instant lastCommitAt;

    @Column(nullable = false)
    private int prMergedCount;

    @Column(nullable = false)
    private int prOpenCount;

    @Column(nullable = false)
    private int prClosedCount;

    @Column(nullable = false)
    private Instant syncedAt;

    public void updateStats(
            Integer commitCount,
            Integer prCount,
            Integer issueCount,
            Integer reviewCount,
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
        this.commitCount = commitCount;
        this.prCount = prCount;
        this.issueCount = issueCount;
        this.reviewCount = reviewCount;
        this.additions = additions;
        this.deletions = deletions;
        this.topCommitRepo = topCommitRepo;
        this.topPrRepo = topPrRepo;
        this.firstCommitAt = firstCommitAt;
        this.lastCommitAt = lastCommitAt;
        this.prMergedCount = prMergedCount;
        this.prOpenCount = prOpenCount;
        this.prClosedCount = prClosedCount;
        this.syncedAt = syncedAt;
    }
}
