package com.process.clash.adapter.persistence.user.userexphistory;

import com.process.clash.application.compete.my.data.Streak;
import com.process.clash.application.compete.my.data.UserEarnedExp;
import com.process.clash.application.compete.my.data.Variation;
import com.process.clash.application.ranking.data.UserRanking;
import org.springframework.data.domain.Pageable;
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
        WHERE fk_user_id IN (:userIds)
          AND date >= :startDate
          AND date < :endDate
        ORDER BY fk_user_id, date ASC
    """, nativeQuery = true)
    List<Object[]> findDailyDataByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
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
        WHERE fk_user_id IN (:userIds)
          AND date >= date_trunc('week', CAST(:startDate AS date))
          AND date < :endDate
        GROUP BY fk_user_id, date_trunc('week', date)
        ORDER BY fk_user_id, date_trunc('week', date) ASC
    """, nativeQuery = true)
    List<Object[]> findWeeklyDataByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
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
        WHERE fk_user_id IN (:userIds)
          AND date >= date_trunc('month', CAST(:startDate AS date))
          AND date < :endDate
        GROUP BY fk_user_id, date_trunc('month', date)
        ORDER BY fk_user_id, date_trunc('month', date) ASC
    """, nativeQuery = true)
    List<Object[]> findMonthlyDataByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
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

    @Query(value = """
        SELECT 
            b.id AS battleId,
            COALESCE(AVG(ueh.earn_exp), 0) AS avgExp
        FROM user_exp_history ueh
        JOIN battles b ON b.id IN (:battleIds)
            AND ueh.fk_user_id = :userId
            AND ueh.date >= b.start_date
            AND ueh.date <= b.end_date
        GROUP BY b.id
    """, nativeQuery = true)
    List<Object[]> findAverageExpForBattles(
            @Param("userId") Long userId,
            @Param("battleIds") List<Long> battleIds
    );


    @Query("""
        select new com.process.clash.application.compete.my.data.Streak(
                ux.date,
                cast(sum(ux.earnExp) as int)
            )
        from UserExpHistoryJpaEntity ux
        where ux.user.id = :userId
            and ux.date >= :startDate
            and ux.date < :endDate
            and ux.actingCategory <> 'SEASON_RESET'
        group by ux.date
        order by ux.date asc
    """)
    List<Streak> findStreakByUserId(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        select new com.process.clash.application.compete.my.data.Variation(
                month(ux.date),
                cast(avg(ux.earnExp) as double)
            )
        from UserExpHistoryJpaEntity ux
        where ux.user.id = :userId
            and ux.date >= :startDate
            and ux.date < :endDate
            and ux.actingCategory <> 'SEASON_RESET'
        group by month(ux.date)
        order by month(ux.date) asc
    """)
    List<Variation> findVariationByUserId(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        select new com.process.clash.application.compete.my.data.UserEarnedExp(
                ux.date,
                avg(ux.earnExp)
            )
        from UserExpHistoryJpaEntity ux
        where ux.user.id = :id
            and ux.date > :startDate
            and ux.date <= :endDate
            and ux.actingCategory <> 'SEASON_RESET'
        group by ux.date
        order by ux.date asc
    """)
    List<UserEarnedExp> findUserDailyEarnedExpByUserIdAndPeriod(
            @Param("id") Long id,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    @Query("""
        select new com.process.clash.application.compete.my.data.UserEarnedExp(
                min(ux.date),
                avg(ux.earnExp)
            )
        from UserExpHistoryJpaEntity ux
        where ux.user.id = :id
            and ux.date >= function('date_trunc', 'week', cast(:startDate as date))
            and ux.date <= :endDate
            and ux.actingCategory <> 'SEASON_RESET'
        group by function('date_trunc', 'week', ux.date)
        order by function('date_trunc', 'week', ux.date) asc
    """)
    List<UserEarnedExp> findUserWeeklyEarnedExpByUserIdAndPeriod(
            @Param("id") Long id,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    @Query("""
        select new com.process.clash.application.compete.my.data.UserEarnedExp(
                min(ux.date),
                avg(ux.earnExp)
            )
        from UserExpHistoryJpaEntity ux
        where ux.user.id = :id
            and ux.date >= function('date_trunc', 'month', cast(:startDate as date))
            and ux.date <= :endDate
            and ux.actingCategory <> 'SEASON_RESET'
        group by function('date_trunc', 'month', ux.date)
        order by function('date_trunc', 'month', ux.date) asc
    """)
    List<UserEarnedExp> findUserMonthlyEarnedExpByUserIdAndPeriod(
            @Param("id") Long id,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    @Query("""
        select new com.process.clash.application.ranking.data.UserRanking(
                ux.user.id,
                ux.user.name,
                ux.user.profileImage,
                case when count(r) > 0 then true else false end,
                ux.user.username,
                cast(sum(ux.earnExp) as long)
            )
        from UserExpHistoryJpaEntity ux
        left join RivalJpaEntity r on
            (ux.user.id in (r.firstUser.id, r.secondUser.id)
                and :userId in (r.firstUser.id, r.secondUser.id)
                and r.rivalLinkingStatus = 'ACCEPTED')
        where ux.actingCategory <> 'SEASON_RESET'
            and ux.date between :startDate and :endDate
        group by ux.user.id, ux.user.name, ux.user.profileImage, ux.user.username
        order by sum(ux.earnExp) desc
    """)
    List<UserRanking> findExpRankingByUserIdAndPeriod(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}