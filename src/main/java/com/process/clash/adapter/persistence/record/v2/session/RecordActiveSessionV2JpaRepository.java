package com.process.clash.adapter.persistence.record.v2.session;

import com.process.clash.application.ranking.data.UserRanking;
import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordActiveSessionV2JpaRepository extends JpaRepository<RecordActiveSessionV2JpaEntity, Long> {

    Boolean existsByUserIdAndEndedAtIsNull(Long userId);

    @Query("""
        select s
        from RecordActiveSessionV2JpaEntity s
        join fetch s.user
        left join fetch s.developSession ds
        left join fetch s.taskSession ts
        left join fetch ts.subject sub
        left join fetch ts.task task
        where s.id = :sessionId
    """)
    Optional<RecordActiveSessionV2JpaEntity> findByIdWithDetails(@Param("sessionId") Long sessionId);

    @Query("""
        select s
        from RecordActiveSessionV2JpaEntity s
        join fetch s.user
        left join fetch s.developSession ds
        left join fetch s.taskSession ts
        left join fetch ts.subject sub
        left join fetch ts.task task
        where s.user.id = :userId
          and s.endedAt is null
    """)
    Optional<RecordActiveSessionV2JpaEntity> findActiveByUserId(@Param("userId") Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select s
        from RecordActiveSessionV2JpaEntity s
        join fetch s.user
        left join fetch s.developSession ds
        left join fetch s.taskSession ts
        left join fetch ts.subject sub
        left join fetch ts.task task
        where s.user.id = :userId
          and s.endedAt is null
    """)
    Optional<RecordActiveSessionV2JpaEntity> findActiveByUserIdForUpdate(@Param("userId") Long userId);

    @Query("""
        select s
        from RecordActiveSessionV2JpaEntity s
        join fetch s.user
        left join fetch s.developSession ds
        left join fetch s.taskSession ts
        left join fetch ts.subject sub
        left join fetch ts.task task
        where s.user.id = :userId
          and s.startedAt < :endTime
          and (s.endedAt is null or s.endedAt > :startTime)
        order by s.startedAt asc
    """)
    List<RecordActiveSessionV2JpaEntity> findAllOverlappingByUserId(
        @Param("userId") Long userId,
        @Param("startTime") Instant startTime,
        @Param("endTime") Instant endTime
    );

    @Query("""
        select s
        from RecordActiveSessionV2JpaEntity s
        join fetch s.user
        left join fetch s.developSession ds
        left join fetch s.taskSession ts
        left join fetch ts.subject sub
        left join fetch ts.task task
        where s.endedAt is null
    """)
    List<RecordActiveSessionV2JpaEntity> findAllActiveSessions();

    @Query("""
        select s.user.id
        from RecordActiveSessionV2JpaEntity s
        where s.endedAt is null
    """)
    List<Long> findAllActiveUserIds();

    @Query(value = """
        select coalesce(sum(
            extract(epoch from (
                least(coalesce(s.ended_at, :now), :endOfDay) -
                greatest(s.started_at, :startOfDay)
            ))
        ), 0)
        from record_active_sessions_v2 s
        where s.fk_user_id = :userId
            and s.started_at < :endOfDay
            and (s.ended_at is null or s.ended_at > :startOfDay)
    """, nativeQuery = true)
    Long getTotalStudyTimeInSeconds(
        @Param("userId") Long userId,
        @Param("startOfDay") Instant startOfDay,
        @Param("endOfDay") Instant endOfDay,
        @Param("now") Instant now
    );

    @Query(value = """
        select s.fk_user_id as userId,
               coalesce(sum(
                   extract(epoch from (
                       least(coalesce(s.ended_at, :now), :endOfDay) -
                       greatest(s.started_at, :startOfDay)
                   ))
               ), 0) as totalSeconds
        from record_active_sessions_v2 s
        where s.fk_user_id IN :userIds
            and s.started_at < :endOfDay
            and (s.ended_at is null or s.ended_at > :startOfDay)
        group by s.fk_user_id
    """, nativeQuery = true)
    List<UserStudyTimeProjectionV2> getTotalStudyTimeInSecondsByUserIds(
        @Param("userIds") List<Long> userIds,
        @Param("startOfDay") Instant startOfDay,
        @Param("endOfDay") Instant endOfDay,
        @Param("now") Instant now
    );

    interface UserStudyTimeProjectionV2 {
        Long getUserId();
        Long getTotalSeconds();
    }

    @Query(value = """
        select
            u.id as userId,
            u.name as name,
            u.profile_image as profileImage,
            case when count(r.id) > 0 then true else false end as isRival,
            u.username as linkedId,
            cast(
                coalesce(
                    sum(
                        extract(epoch from (
                            least(coalesce(ss.ended_at, :now), :endDate)
                            - greatest(ss.started_at, :startDate)
                        ))
                    ), 0
                ) as bigint
            ) as point
        from record_active_sessions_v2 ss
        inner join users u on u.id = ss.fk_user_id
        left join rivals r on
            (u.id in (r.fk_first_user_id, r.fk_second_user_id)
                and :userId in (r.fk_first_user_id, r.fk_second_user_id)
                and r.rival_linking_status = 'ACCEPTED')
        where ss.started_at < :endDate
            and coalesce(ss.ended_at, :now) >= :startDate
        group by u.id, u.name, u.profile_image, u.username
        order by point desc
    """, nativeQuery = true)
    List<UserRanking> findStudyTimeRankingByUserIdAndPeriod(
        @Param("userId") Long userId,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate,
        @Param("now") Instant now
    );

    @Query(value = """
        SELECT
            s.fk_user_id as userId,
            cast(
                date_trunc(
                    'day',
                    (s.started_at AT TIME ZONE cast(:recordTimezone as text))
                    - make_interval(hours => :dayBoundaryHour)
                ) as date
            ) as recordedDate,
            cast(
                coalesce(
                    sum(
                        extract(epoch from (
                            least(coalesce(s.ended_at, :now), :endDate)
                            - greatest(s.started_at, :startDate)
                        ))
                    ), 0
                ) as bigint
            ) as point
        FROM record_active_sessions_v2 s
        WHERE s.fk_user_id IN :userIds
            AND s.started_at < :endDate
            AND coalesce(s.ended_at, :now) >= :startDate
        GROUP BY s.fk_user_id, date_trunc(
            'day',
            (s.started_at AT TIME ZONE cast(:recordTimezone as text))
            - make_interval(hours => :dayBoundaryHour)
        )
        ORDER BY s.fk_user_id, date_trunc(
            'day',
            (s.started_at AT TIME ZONE cast(:recordTimezone as text))
            - make_interval(hours => :dayBoundaryHour)
        ) ASC
    """, nativeQuery = true)
    List<UserStudyTimePeriodProjectionV2> findDailyStudyTimeByUserIds(
        @Param("userIds") List<Long> userIds,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate,
        @Param("now") Instant now,
        @Param("recordTimezone") String recordTimezone,
        @Param("dayBoundaryHour") int dayBoundaryHour
    );

    @Query(value = """
        SELECT
            s.fk_user_id as userId,
            cast(
                date_trunc(
                    'week',
                    (s.started_at AT TIME ZONE cast(:recordTimezone as text))
                    - make_interval(hours => :dayBoundaryHour)
                ) as date
            ) as recordedDate,
            cast(
                coalesce(
                    sum(
                        extract(epoch from (
                            least(coalesce(s.ended_at, :now), :endDate)
                            - greatest(s.started_at, :startDate)
                        ))
                    ), 0
                ) as bigint
            ) as point
        FROM record_active_sessions_v2 s
        WHERE s.fk_user_id IN :userIds
            AND s.started_at < :endDate
            AND coalesce(s.ended_at, :now) >= :startDate
        GROUP BY s.fk_user_id, date_trunc(
            'week',
            (s.started_at AT TIME ZONE cast(:recordTimezone as text))
            - make_interval(hours => :dayBoundaryHour)
        )
        ORDER BY s.fk_user_id, date_trunc(
            'week',
            (s.started_at AT TIME ZONE cast(:recordTimezone as text))
            - make_interval(hours => :dayBoundaryHour)
        ) ASC
    """, nativeQuery = true)
    List<UserStudyTimePeriodProjectionV2> findWeeklyStudyTimeByUserIds(
        @Param("userIds") List<Long> userIds,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate,
        @Param("now") Instant now,
        @Param("recordTimezone") String recordTimezone,
        @Param("dayBoundaryHour") int dayBoundaryHour
    );

    @Query(value = """
        SELECT
            s.fk_user_id as userId,
            cast(
                date_trunc(
                    'month',
                    (s.started_at AT TIME ZONE cast(:recordTimezone as text))
                    - make_interval(hours => :dayBoundaryHour)
                ) as date
            ) as recordedDate,
            cast(
                coalesce(
                    sum(
                        extract(epoch from (
                            least(coalesce(s.ended_at, :now), :endDate)
                            - greatest(s.started_at, :startDate)
                        ))
                    ), 0
                ) as bigint
            ) as point
        FROM record_active_sessions_v2 s
        WHERE s.fk_user_id IN :userIds
            AND s.started_at < :endDate
            AND coalesce(s.ended_at, :now) >= :startDate
        GROUP BY s.fk_user_id, date_trunc(
            'month',
            (s.started_at AT TIME ZONE cast(:recordTimezone as text))
            - make_interval(hours => :dayBoundaryHour)
        )
        ORDER BY s.fk_user_id, date_trunc(
            'month',
            (s.started_at AT TIME ZONE cast(:recordTimezone as text))
            - make_interval(hours => :dayBoundaryHour)
        ) ASC
    """, nativeQuery = true)
    List<UserStudyTimePeriodProjectionV2> findMonthlyStudyTimeByUserIds(
        @Param("userIds") List<Long> userIds,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate,
        @Param("now") Instant now,
        @Param("recordTimezone") String recordTimezone,
        @Param("dayBoundaryHour") int dayBoundaryHour
    );

    interface UserStudyTimePeriodProjectionV2 {
        Long getUserId();
        java.sql.Date getRecordedDate();
        Long getPoint();
    }
}
