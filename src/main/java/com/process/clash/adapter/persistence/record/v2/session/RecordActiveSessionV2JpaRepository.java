package com.process.clash.adapter.persistence.record.v2.session;

import jakarta.persistence.LockModeType;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
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

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select s
        from RecordActiveSessionV2JpaEntity s
        join fetch s.user
        left join fetch s.developSession ds
        left join fetch s.taskSession ts
        left join fetch ts.subject sub
        left join fetch ts.task task
        where s.sessionType = :sessionType
          and s.endedAt is null
          and s.startedAt <= :startedBefore
        order by s.startedAt asc
    """)
    List<RecordActiveSessionV2JpaEntity> findAllActiveBySessionTypeAndStartedAtBeforeForUpdate(
        @Param("sessionType") RecordSessionTypeV2 sessionType,
        @Param("startedBefore") Instant startedBefore
    );

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
        where s.user.id in :userIds
          and s.endedAt is null
    """)
    List<RecordActiveSessionV2JpaEntity> findAllActiveByUserIds(@Param("userIds") List<Long> userIds);

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
    List<RecordActiveSessionV2JpaEntity> findAllActive();

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
    List<UserStudyTimeProjection> getTotalStudyTimeInSecondsByUserIds(
        @Param("userIds") List<Long> userIds,
        @Param("startOfDay") Instant startOfDay,
        @Param("endOfDay") Instant endOfDay,
        @Param("now") Instant now
    );

    interface UserStudyTimeProjection {
        Long getUserId();
        Long getTotalSeconds();
    }

    interface StudyTimeRankingRow {
        Long getUserId();
        String getName();
        String getProfileImage();
        Boolean getIsRival();
        String getLinkedId();
        Long getPoint();
        String getRankTier();
        String getExpTier();
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
                        case when ss.started_at is not null then
                            extract(epoch from (
                                least(coalesce(ss.ended_at, :now), :endDate)
                                - greatest(ss.started_at, :startDate)
                            ))
                        end
                    ), 0
                ) as bigint
            ) as point,
            u.current_rank_tier as rankTier,
            u.current_exp_tier as expTier
        from users u
        left join record_active_sessions_v2 ss
            on ss.fk_user_id = u.id
            and ss.started_at < :endDate
            and coalesce(ss.ended_at, :now) >= :startDate
        left join rivals r on
            ((r.fk_first_user_id = u.id and r.fk_second_user_id = :userId)
                or (r.fk_second_user_id = u.id and r.fk_first_user_id = :userId))
            and r.rival_linking_status = 'ACCEPTED'
        group by u.id, u.name, u.profile_image, u.username, u.current_rank_tier, u.current_exp_tier
        order by point desc
    """, nativeQuery = true)
    List<StudyTimeRankingRow> findStudyTimeRankingByUserIdAndPeriod(
        @Param("userId") Long userId,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate,
        @Param("now") Instant now
    );

    @Query(value = """
        SELECT
            s.fk_user_id as userId,
            cast(date_trunc('day', (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours') as date) as recordedDate,
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
        GROUP BY s.fk_user_id, date_trunc('day', (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours')
        ORDER BY s.fk_user_id, date_trunc('day', (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours') ASC
    """, nativeQuery = true)
    List<Object[]> findDailyStudyTimeByUserIds(
        @Param("userIds") List<Long> userIds,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate,
        @Param("now") Instant now
    );

    @Query(value = """
        SELECT
            s.fk_user_id as userId,
            cast(date_trunc('month', (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours') as date) + (floor((extract(day from (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours') - 1) / 7) * 7)::int as recordedDate,
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
        GROUP BY s.fk_user_id, cast(date_trunc('month', (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours') as date) + (floor((extract(day from (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours') - 1) / 7) * 7)::int
        ORDER BY s.fk_user_id, cast(date_trunc('month', (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours') as date) + (floor((extract(day from (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours') - 1) / 7) * 7)::int ASC
    """, nativeQuery = true)
    List<Object[]> findWeeklyStudyTimeByUserIds(
        @Param("userIds") List<Long> userIds,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate,
        @Param("now") Instant now
    );

    @Query(value = """
        SELECT
            s.fk_user_id as userId,
            cast(date_trunc('month', (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours') as date) as recordedDate,
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
        GROUP BY s.fk_user_id, date_trunc('month', (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours')
        ORDER BY s.fk_user_id, date_trunc('month', (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours') ASC
    """, nativeQuery = true)
    List<Object[]> findMonthlyStudyTimeByUserIds(
        @Param("userIds") List<Long> userIds,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate,
        @Param("now") Instant now
    );
}
