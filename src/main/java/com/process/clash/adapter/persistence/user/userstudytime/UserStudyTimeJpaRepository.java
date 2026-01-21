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

    @Query(value = """
        SELECT 
            fk_user_id as userId,
            date,
            totalStudyTimeSeconds as point
        FROM user_study_times
        WHERE fk_user_id IN :userIds
        AND date >= :startDate
        AND date < :endDate
        ORDER BY fk_user_id, date ASC
    """, nativeQuery = true)
    List<Object[]> findDailyDataByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query(value = """
        SELECT 
            fk_user_id as userId,
            DATE_SUB(date, INTERVAL (DAYOFWEEK(date) - 2) DAY) as date,
            AVG(totalStudyTimeSeconds) as point
        FROM user_study_times
        WHERE fk_user_id IN :userIds
        AND date >= :startDate
        AND date < :endDate
        GROUP BY fk_user_id, YEARWEEK(date)
        ORDER BY fk_user_id, MIN(date) ASC
    """, nativeQuery = true)
    List<Object[]> findWeeklyDataByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query(value = """
        SELECT 
            fk_user_id as userId,
            DATE_FORMAT(date, '%Y-%m-01') as date,
            AVG(totalStudyTimeSeconds) as point
        FROM user_study_times
        WHERE fk_user_id IN :userIds
        AND date >= :startDate
        AND date < :endDate
        GROUP BY fk_user_id, YEAR(date), MONTH(date)
        ORDER BY fk_user_id, MIN(date) ASC
    """, nativeQuery = true)
    List<Object[]> findMonthlyDataByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

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
