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

    @Column(nullable = false)
    private Instant syncedAt;
}
