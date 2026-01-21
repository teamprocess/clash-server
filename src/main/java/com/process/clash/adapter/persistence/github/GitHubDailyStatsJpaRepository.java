package com.process.clash.adapter.persistence.github;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GitHubDailyStatsJpaRepository extends JpaRepository<GitHubDailyStatsJpaEntity, GitHubDailyStatsId> {

    List<GitHubDailyStatsJpaEntity> findByUserIdAndStudyDateIn(Long userId, List<LocalDate> studyDates);

    Optional<GitHubDailyStatsJpaEntity> findByUserIdAndStudyDate(Long userId, LocalDate studyDate);

    // DAY: 여러 유저의 일별 깃허브 기여도 데이터
    @Query(value = """
        SELECT
            user_id AS "userId",
            study_date AS "date",
            (commit_count + pr_count + review_count + issue_count) AS point
        FROM github_daily_stats
        WHERE user_id = ANY(:userIds)
          AND study_date >= :startDate
          AND study_date < :endDate
        ORDER BY user_id, study_date ASC
    """, nativeQuery = true)
    List<Object[]> findDailyContributionsByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // WEEK: 여러 유저의 주별 평균 깃허브 기여도
    @Query(value = """
        SELECT
            user_id AS "userId",
            date_trunc('week', study_date) AS "date",
            AVG(commit_count + pr_count + review_count + issue_count) AS point
        FROM github_daily_stats
        WHERE user_id = ANY(:userIds)
          AND study_date >= :startDate
          AND study_date < :endDate
        GROUP BY user_id, date_trunc('week', study_date)
        ORDER BY user_id, date_trunc('week', study_date) ASC
    """, nativeQuery = true)
    List<Object[]> findWeeklyContributionsByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // MONTH: 여러 유저의 월별 평균 깃허브 기여도
    @Query(value = """
        SELECT
            user_id AS "userId",
            date_trunc('month', study_date) AS "date",
            AVG(commit_count + pr_count + review_count + issue_count) AS point
        FROM github_daily_stats
        WHERE user_id = ANY(:userIds)
          AND study_date >= :startDate
          AND study_date < :endDate
        GROUP BY user_id, date_trunc('month', study_date)
        ORDER BY user_id, date_trunc('month', study_date) ASC
    """, nativeQuery = true)
    List<Object[]> findMonthlyContributionsByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // 기간 내 평균 기여도
    @Query(value = """
        SELECT COALESCE(AVG(commit_count + pr_count + review_count + issue_count), 0)
        FROM github_daily_stats
        WHERE user_id = :userId
          AND study_date >= :startDate
          AND study_date < :endDate
    """, nativeQuery = true)
    double findAverageCommitsByUserIdAndPeriod(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
