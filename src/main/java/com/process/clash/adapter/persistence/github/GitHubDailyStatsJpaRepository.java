package com.process.clash.adapter.persistence.github;

import com.process.clash.application.compete.my.data.Streak;
import com.process.clash.application.compete.my.data.Variation;
import com.process.clash.application.github.data.GitHubDailyContributionDto;
import com.process.clash.application.ranking.data.UserRanking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GitHubDailyStatsJpaRepository extends JpaRepository<GitHubDailyStatsJpaEntity, GitHubDailyStatsId> {

    List<GitHubDailyStatsJpaEntity> findByUserIdAndStudyDateIn(Long userId, List<LocalDate> studyDates);

    Optional<GitHubDailyStatsJpaEntity> findByUserIdAndStudyDate(Long userId, LocalDate studyDate);

    @Query("""
        select new com.process.clash.application.github.data.GitHubDailyContributionDto(
                g.studyDate,
                g.commitCount + g.prCount + g.reviewCount + g.issueCount
            )
        from GitHubDailyStatsJpaEntity g
        where g.userId = :userId
          and g.studyDate >= :startDate
          and g.studyDate < :endDate
        order by g.studyDate asc
    """)
    List<GitHubDailyContributionDto> findDailyContributionsByUserId(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    // DAY: 여러 유저의 일별 깃허브 기여도 데이터
    @Query(value = """
        SELECT
            user_id AS userId,
            study_date AS recordedDate,
            (commit_count + pr_count + review_count + issue_count) AS point
        FROM (
            SELECT
                user_id,
                study_date,
                commit_count,
                pr_count,
                review_count,
                issue_count,
                ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY study_date DESC) as rn
            FROM github_daily_stats
            WHERE user_id IN (:userIds)
              AND study_date >= :startDate
              AND study_date < :endDate
        ) subquery
        WHERE rn <= 10
        ORDER BY user_id, study_date ASC
    """, nativeQuery = true)
    List<Object[]> findDailyContributionsByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // WEEK: 여러 유저의 주별 평균 깃허브 기여도 (1~7일=1주차, 8~14일=2주차, ...)
    @Query(value = """
        SELECT
            user_id AS userId,
            week_start AS recordedDate,
            point
        FROM (
            SELECT
                user_id,
                cast(date_trunc('month', study_date) as date) + (floor((extract(day from study_date) - 1) / 7) * 7)::int AS week_start,
                AVG(commit_count + pr_count + review_count + issue_count) AS point,
                ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY cast(date_trunc('month', study_date) as date) + (floor((extract(day from study_date) - 1) / 7) * 7)::int DESC) as rn
            FROM github_daily_stats
            WHERE user_id IN (:userIds)
              AND study_date >= :startDate
              AND study_date <= :endDate
            GROUP BY user_id, cast(date_trunc('month', study_date) as date) + (floor((extract(day from study_date) - 1) / 7) * 7)::int
        ) subquery
        WHERE rn <= 10
        ORDER BY user_id, week_start ASC
    """, nativeQuery = true)
    List<Object[]> findWeeklyContributionsByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // MONTH: 여러 유저의 월별 평균 깃허브 기여도
    @Query(value = """
        SELECT
            user_id AS userId,
            month_start AS recordedDate,
            point
        FROM (
            SELECT
                user_id,
                cast(date_trunc('month', study_date) as date) AS month_start,
                AVG(commit_count + pr_count + review_count + issue_count) AS point,
                ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY date_trunc('month', study_date) DESC) as rn
            FROM github_daily_stats
            WHERE user_id IN (:userIds)
              AND study_date >= date_trunc('month', CAST(:startDate AS date))
              AND study_date < :endDate
            GROUP BY user_id, date_trunc('month', study_date)
        ) subquery
        WHERE rn <= 10
        ORDER BY user_id, month_start ASC
    """, nativeQuery = true)
    List<Object[]> findMonthlyContributionsByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // 기간 내 총 커밋 수
    @Query(value = """
        SELECT COALESCE(SUM(commit_count), 0)
        FROM github_daily_stats
        WHERE user_id = :userId
          AND study_date >= :startDate
          AND study_date < :endDate
    """, nativeQuery = true)
    double findTotalCommitCountByUserIdAndPeriod(
            @Param("userId") Long userId,
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
    double findAverageContributionByUserIdAndPeriod(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        select new com.process.clash.application.compete.my.data.Streak(
                g.studyDate,
                g.commitCount + g.prCount + g.reviewCount + g.issueCount
            )
        from GitHubDailyStatsJpaEntity g
        where g.userId = :userId
            and g.studyDate >= :startDate
            and g.studyDate < :endDate
        order by g.studyDate asc
    """)
    List<Streak> findStreakByUserId(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        select new com.process.clash.application.compete.my.data.Variation(
                month(g.studyDate),
                cast(coalesce(sum(g.commitCount + g.prCount + g.reviewCount + g.issueCount), 0) as double)
            )
        from GitHubDailyStatsJpaEntity g
        where g.userId = :userId
            and g.studyDate >= :startDate
            and g.studyDate < :endDate
        group by month(g.studyDate)
        order by month(g.studyDate) asc
    """)
    List<Variation> findVariationByUserId(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        select new com.process.clash.application.ranking.data.UserRanking(
                ug.user.id,
                ug.user.name,
                ug.user.profileImage,
                case when count(r) > 0 then true else false end,
                ug.gitHubId,
                cast(
                    coalesce(
                        sum(g.commitCount + g.issueCount + g.prCount + g.reviewCount), 0
                    ) as long
                )
            )
        from UserGitHubJpaEntity ug
        left join GitHubDailyStatsJpaEntity g on
            g.userId = ug.user.id
            and g.studyDate between :startDate and :endDate
        left join RivalJpaEntity r on
            (ug.user.id in (r.firstUser.id, r.secondUser.id)
                and :userId in (r.firstUser.id, r.secondUser.id)
                and r.rivalLinkingStatus = 'ACCEPTED')
        group by ug.user.id, ug.user.name, ug.user.profileImage, ug.user.username, ug.gitHubId
        order by sum(g.commitCount + g.issueCount + g.prCount + g.reviewCount) desc
    """)
    List<UserRanking> findGitHubRankingByUserIdAndPeriod(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
