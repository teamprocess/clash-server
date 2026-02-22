package com.process.clash.adapter.persistence.user.userstudytime;

import com.process.clash.application.compete.my.data.Streak;
import com.process.clash.application.compete.my.data.Variation;
import com.process.clash.application.user.userstudytime.data.UserStudyTimeDailyDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserStudyTimeJpaRepository extends JpaRepository<UserStudyTimeJpaEntity, Long> {

    Optional<UserStudyTimeJpaEntity> findByUserIdAndDate(Long userId, LocalDate date);

    @Query("""
        select new com.process.clash.application.user.userstudytime.data.UserStudyTimeDailyDto(
                us.date,
                us.totalStudyTimeSeconds
            )
        from UserStudyTimeJpaEntity us
        where us.user.id = :userId
          and us.date >= :startDate
          and us.date < :endDate
        order by us.date asc
    """)
    List<UserStudyTimeDailyDto> findDailyDataByUserId(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    /**
     * DAY: 유저별 일별 학습시간
     */
    @Query(value = """
        SELECT
            fk_user_id as userId,
            date as recordedDate,
            total_study_time_seconds as point
        FROM (
            SELECT
                fk_user_id,
                date,
                total_study_time_seconds,
                ROW_NUMBER() OVER (PARTITION BY fk_user_id ORDER BY date DESC) as rn
            FROM user_study_times
            WHERE fk_user_id IN (:userIds)
              AND date >= :startDate
              AND date < :endDate
        ) subquery
        WHERE rn <= 10
        ORDER BY fk_user_id, date ASC
    """, nativeQuery = true)
    List<Object[]> findDailyDataByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * WEEK: 유저별 주별 평균 학습시간
     */
    @Query(value = """
        SELECT
            fk_user_id as userId,
            week_start as recordedDate,
            point
        FROM (
            SELECT
                fk_user_id,
                cast(date_trunc('week', date) as date) as week_start,
                AVG(total_study_time_seconds) as point,
                ROW_NUMBER() OVER (PARTITION BY fk_user_id ORDER BY date_trunc('week', date) DESC) as rn
            FROM user_study_times
            WHERE fk_user_id IN (:userIds)
              AND date >= date_trunc('week', CAST(:startDate AS date))
              AND date < :endDate
            GROUP BY fk_user_id, date_trunc('week', date)
        ) subquery
        WHERE rn <= 10
        ORDER BY fk_user_id, week_start ASC
    """, nativeQuery = true)
    List<Object[]> findWeeklyDataByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * MONTH: 유저별 월별 평균 학습시간
     */
    @Query(value = """
        SELECT
            fk_user_id as userId,
            month_start as recordedDate,
            point
        FROM (
            SELECT
                fk_user_id,
                cast(date_trunc('month', date) as date) as month_start,
                AVG(total_study_time_seconds) as point,
                ROW_NUMBER() OVER (PARTITION BY fk_user_id ORDER BY date_trunc('month', date) DESC) as rn
            FROM user_study_times
            WHERE fk_user_id IN (:userIds)
              AND date >= date_trunc('month', CAST(:startDate AS date))
              AND date < :endDate
            GROUP BY fk_user_id, date_trunc('month', date)
        ) subquery
        WHERE rn <= 10
        ORDER BY fk_user_id, month_start ASC
    """, nativeQuery = true)
    List<Object[]> findMonthlyDataByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * 기간 내 평균 학습시간
     */
    @Query(value = """
        SELECT COALESCE(AVG(total_study_time_seconds), 0)
        FROM user_study_times
        WHERE fk_user_id = :userId
          AND date >= :startDate
          AND date < :endDate
    """, nativeQuery = true)
    double findAverageStudyTimeByUserIdAndPeriod(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        select new com.process.clash.application.compete.my.data.Streak(
                us.date,
                cast(us.totalStudyTimeSeconds as int)
            )
        from UserStudyTimeJpaEntity us
        where us.user.id = :userId
            and us.date >= :startDate
            and us.date < :endDate
        order by us.date asc
    """)
    List<Streak> findStreakByUserId(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        select new com.process.clash.application.compete.my.data.Variation(
                month(us.date),
                cast(coalesce(sum(us.totalStudyTimeSeconds), 0) as double)
            )
        from UserStudyTimeJpaEntity us
        where us.user.id = :userId
            and us.date >= :startDate
            and us.date < :endDate
        group by month(us.date)
        order by month(us.date) asc
    """)
    List<Variation> findVariationByUserId(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
