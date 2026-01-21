package com.process.clash.adapter.persistence.user.userexphistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserExpHistoryJpaRepository extends JpaRepository<UserExpHistoryJpaEntity, Long> {

    /**
     * DAY: 유저별 일별 경험치
     */
    @Query(value = """
        SELECT 
            fk_user_id AS "userId",
            date AS "date",
            earn_exp AS point
        FROM user_exp_history
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
     * WEEK: 유저별 주별 평균 경험치
     */
    @Query(value = """
        SELECT 
            fk_user_id AS "userId",
            date_trunc('week', date) AS "date",
            AVG(earn_exp) AS point
        FROM user_exp_history
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
     * MONTH: 유저별 월별 평균 경험치
     */
    @Query(value = """
        SELECT 
            fk_user_id AS "userId",
            date_trunc('month', date) AS "date",
            AVG(earn_exp) AS point
        FROM user_exp_history
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
     * 기간 내 평균 경험치 (단일 값)
     */
    @Query(value = """
        SELECT COALESCE(AVG(earn_exp), 0) AS point
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
