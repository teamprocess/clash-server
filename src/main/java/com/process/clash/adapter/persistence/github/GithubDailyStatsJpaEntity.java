package com.process.clash.adapter.persistence.github;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
public class GithubDailyStatsJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "study_date", nullable = false)
    private LocalDate studyDate;

    @Column(name = "commit_count", nullable = false)
    private int commitCount;

    @Column(name = "pr_count", nullable = false)
    private int prCount;

    @Column(name = "issue_count", nullable = false)
    private int issueCount;

    @Column(name = "review_count", nullable = false)
    private int reviewCount;

    @Column(name = "additions", nullable = false)
    private long additions;

    @Column(name = "deletions", nullable = false)
    private long deletions;

    @Column(name = "synced_at", nullable = false)
    private Instant syncedAt;
}
