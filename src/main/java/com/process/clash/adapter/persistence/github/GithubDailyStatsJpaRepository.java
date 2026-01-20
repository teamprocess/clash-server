package com.process.clash.adapter.persistence.github;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GithubDailyStatsJpaRepository extends JpaRepository<GithubDailyStatsJpaEntity, Long> {
    List<GithubDailyStatsJpaEntity> findByUserIdAndStudyDateIn(Long userId, List<LocalDate> studyDates);

    Optional<GithubDailyStatsJpaEntity> findByUserIdAndStudyDate(Long userId, LocalDate studyDate);

    // DAY: 여러 유저의 일별 깃허브 기여도 데이터
    @Query(value = """
        SELECT 
            fk_user_id as userId,
            study_date as date,
            (commit_count + pr_count + reviewed_pr_count) as point
        FROM github_daily_stats
        WHERE fk_user_id IN :userIds
        AND study_date >= :startDate
        AND study_date < :endDate
        ORDER BY fk_user_id, study_date ASC
    """, nativeQuery = true)
    List<Object[]> findDailyContributionsByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // WEEK: 여러 유저의 주별 평균 깃허브 기여도
    @Query(value = """
        SELECT 
            fk_user_id as userId,
            DATE_SUB(study_date, INTERVAL (DAYOFWEEK(study_date) - 2) DAY) as date,
            AVG(commit_count + pr_count + reviewed_pr_count) as point
        FROM github_daily_stats
        WHERE fk_user_id IN :userIds
        AND study_date >= :startDate
        AND study_date < :endDate
        GROUP BY fk_user_id, YEARWEEK(study_date)
        ORDER BY fk_user_id, MIN(study_date) ASC
    """, nativeQuery = true)
    List<Object[]> findWeeklyContributionsByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // MONTH: 여러 유저의 월별 평균 깃허브 기여도
    @Query(value = """
        SELECT 
            fk_user_id as userId,
            DATE_FORMAT(study_date, '%Y-%m-01') as date,
            AVG(commit_count + pr_count + reviewed_pr_count) as point
        FROM github_daily_stats
        WHERE fk_user_id IN :userIds
        AND study_date >= :startDate
        AND study_date < :endDate
        GROUP BY fk_user_id, YEAR(study_date), MONTH(study_date)
        ORDER BY fk_user_id, MIN(study_date) ASC
    """, nativeQuery = true)
    List<Object[]> findMonthlyContributionsByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
