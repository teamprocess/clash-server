package com.process.clash.adapter.persistence.user.userexphistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserExpHistoryJpaRepository extends JpaRepository<UserExpHistoryJpaEntity, Long> {

    @Query(value = """
        SELECT 
            fk_user_id as userId,
            date,
            earn_exp as point
        FROM user_exp_history
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
            AVG(earn_exp) as point
        FROM user_exp_history
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
            AVG(earn_exp) as point
        FROM user_exp_history
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
        SELECT AVG(earn_exp) as point
        FROM user_exp_history
        WHERE fk_user_id = :userId
          AND date >= :startDate
          AND date < :endDate
    """, nativeQuery = true)
    double findAverageExpByUserIdAndPeriod(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}