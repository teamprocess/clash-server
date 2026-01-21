package com.process.clash.adapter.persistence.user.userstudytime;

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

    /**
     * DAY: 유저별 일별 학습시간
     */
    @Query(value = """
        SELECT 
            fk_user_id as "userId",
            date as "date",
            totalStudyTimeSeconds as point
        FROM user_study_times
        WHERE fk_user_id = ANY(:userIds)
          AND date >= :startDate
          AND date < :endDate
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
            fk_user_id as "userId",
            date_trunc('week', date) as "date",
            AVG(totalStudyTimeSeconds) as point
        FROM user_study_times
        WHERE fk_user_id = ANY(:userIds)
          AND date >= :startDate
          AND date < :endDate
        GROUP BY fk_user_id, date_trunc('week', date)
        ORDER BY fk_user_id, date_trunc('week', date) ASC
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
            fk_user_id as "userId",
            date_trunc('month', date) as "date",
            AVG(totalStudyTimeSeconds) as point
        FROM user_study_times
        WHERE fk_user_id = ANY(:userIds)
          AND date >= :startDate
          AND date < :endDate
        GROUP BY fk_user_id, date_trunc('month', date)
        ORDER BY fk_user_id, date_trunc('month', date) ASC
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
}
