package com.process.clash.adapter.persistence.recordsession;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.process.clash.application.ranking.data.UserRanking;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordSessionJpaRepository extends JpaRepository<RecordSessionJpaEntity, Long> {

    @EntityGraph(attributePaths = {"user", "task"})
    List<RecordSessionJpaEntity> findAllByUserId(Long userId);

    Boolean existsByUserIdAndEndedAtIsNull(Long userId);

    Boolean existsByTaskIdAndEndedAtIsNull(Long taskId);

    @EntityGraph(attributePaths = {"user", "task"})
    Optional<RecordSessionJpaEntity> findByUserIdAndEndedAtIsNull(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select s
        from RecordSessionJpaEntity s
        left join fetch s.task
        join fetch s.user
        where s.user.id = :userId
            and s.endedAt is null
    """)
    Optional<RecordSessionJpaEntity> findActiveByUserIdForUpdate(@Param("userId") Long userId);

    @EntityGraph(attributePaths = {"user", "task"})
    List<RecordSessionJpaEntity> findAllByEndedAtIsNull();

    @Query("""
        select s
        from RecordSessionJpaEntity s
        join fetch s.user
        left join fetch s.task
        where s.user.id = :userId
            and s.startedAt < :endTime
            and (s.endedAt is null or s.endedAt > :startTime)
    """)
    List<RecordSessionJpaEntity> findAllOverlappingByUserId(
            @Param("userId") Long userId,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime
    );

    @Query(value = """
        select coalesce(sum(
            extract(epoch from (
                least(coalesce(s.ended_at, :now), :endOfDay) -
                greatest(s.started_at, :startOfDay)
            ))
        ), 0)
        from record_sessions s
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
        from record_sessions s
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
                            least(coalesce(ss.ended_at, current_timestamp), :endDate)
                            - greatest(ss.started_at, :startDate)
                        ))
                    ), 0
                ) as bigint
            ) as point
        from record_sessions ss
        inner join users u on u.id = ss.fk_user_id
        left join rivals r on
            (u.id in (r.fk_first_user_id, r.fk_second_user_id)
                and :userId in (r.fk_first_user_id, r.fk_second_user_id)
                and r.rival_linking_status = 'ACCEPTED')
        where ss.started_at < :endDate
            and coalesce(ss.ended_at, current_timestamp) >= :startDate
        group by u.id, u.name, u.profile_image, u.username
        order by point desc
    """, nativeQuery = true)
    List<UserRanking> findStudyTimeRankingByUserIdAndPeriod(
            @Param("userId") Long userId,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate
    );

    // 일별 학습시간 집계 (여러 유저)
    @Query(value = """
        SELECT
            s.fk_user_id as userId,
            cast(date_trunc('day', (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours') as date) as recordedDate,
            cast(
                coalesce(
                    sum(
                        extract(epoch from (
                            least(coalesce(s.ended_at, current_timestamp), :endDate)
                            - greatest(s.started_at, :startDate)
                        ))
                    ), 0
                ) as bigint
            ) as point
        FROM record_sessions s
        WHERE s.fk_user_id IN :userIds
            AND s.started_at < :endDate
            AND coalesce(s.ended_at, current_timestamp) >= :startDate
        GROUP BY s.fk_user_id, date_trunc('day', (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours')
        ORDER BY s.fk_user_id, date_trunc('day', (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours') ASC
    """, nativeQuery = true)
    List<Object[]> findDailyStudyTimeByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate
    );

    // 주별 총 학습시간 집계 (여러 유저) (1~7일=1주차, 8~14일=2주차, ...)
    @Query(value = """
        SELECT
            s.fk_user_id as userId,
            cast(date_trunc('month', (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours') as date) + (floor((extract(day from (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours') - 1) / 7) * 7)::int as recordedDate,
            cast(
                coalesce(
                    sum(
                        extract(epoch from (
                            least(coalesce(s.ended_at, current_timestamp), :endDate)
                            - greatest(s.started_at, :startDate)
                        ))
                    ), 0
                ) as bigint
            ) as point
        FROM record_sessions s
        WHERE s.fk_user_id IN :userIds
            AND s.started_at < :endDate
            AND coalesce(s.ended_at, current_timestamp) >= :startDate
        GROUP BY s.fk_user_id, cast(date_trunc('month', (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours') as date) + (floor((extract(day from (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours') - 1) / 7) * 7)::int
        ORDER BY s.fk_user_id, cast(date_trunc('month', (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours') as date) + (floor((extract(day from (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours') - 1) / 7) * 7)::int ASC
    """, nativeQuery = true)
    List<Object[]> findWeeklyStudyTimeByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate
    );

    // 월별 총 학습시간 집계 (여러 유저)
    @Query(value = """
        SELECT
            s.fk_user_id as userId,
            cast(date_trunc('month', (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours') as date) as recordedDate,
            cast(
                coalesce(
                    sum(
                        extract(epoch from (
                            least(coalesce(s.ended_at, current_timestamp), :endDate)
                            - greatest(s.started_at, :startDate)
                        ))
                    ), 0
                ) as bigint
            ) as point
        FROM record_sessions s
        WHERE s.fk_user_id IN :userIds
            AND s.started_at < :endDate
            AND coalesce(s.ended_at, current_timestamp) >= :startDate
        GROUP BY s.fk_user_id, date_trunc('month', (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours')
        ORDER BY s.fk_user_id, date_trunc('month', (s.started_at AT TIME ZONE 'Asia/Seoul') - interval '6 hours') ASC
    """, nativeQuery = true)
    List<Object[]> findMonthlyStudyTimeByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate
    );
}