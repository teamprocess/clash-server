package com.process.clash.adapter.persistence.studysession;

import java.time.LocalDateTime;
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
public interface StudySessionJpaRepository extends JpaRepository<StudySessionJpaEntity, Long> {

    @EntityGraph(attributePaths = {"user", "task"})
    List<StudySessionJpaEntity> findAllByUserId(Long userId);

    Boolean existsByUserIdAndEndedAtIsNull(Long userId);

    Boolean existsByTaskIdAndEndedAtIsNull(Long taskId);

    @EntityGraph(attributePaths = {"user", "task"})
    Optional<StudySessionJpaEntity> findByUserIdAndEndedAtIsNull(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select s
        from StudySessionJpaEntity s
        left join fetch s.task
        join fetch s.user
        where s.user.id = :userId
            and s.endedAt is null
    """)
    Optional<StudySessionJpaEntity> findActiveByUserIdForUpdate(@Param("userId") Long userId);

    @EntityGraph(attributePaths = {"user", "task"})
    List<StudySessionJpaEntity> findAllByEndedAtIsNull();

    @Query("""
        select s
        from StudySessionJpaEntity s
        join fetch s.user
        left join fetch s.task
        where s.user.id = :userId
            and s.startedAt < :endTime
            and (s.endedAt is null or s.endedAt > :startTime)
    """)
    List<StudySessionJpaEntity> findAllOverlappingByUserId(
        @Param("userId") Long userId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );

    @Query(value = """
        select coalesce(sum(
            extract(epoch from (
                least(coalesce(s.ended_at, cast(:now as timestamp)), cast(:endOfDay as timestamp)) -
                greatest(s.started_at, cast(:startOfDay as timestamp))
            ))
        ), 0)
        from study_sessions s
        where s.fk_user_id = :userId
            and s.started_at < :endOfDay
            and (s.ended_at is null or s.ended_at > :startOfDay)
    """, nativeQuery = true)
    Long getTotalStudyTimeInSeconds(
            @Param("userId") Long userId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            @Param("now") LocalDateTime now
    );

    @Query(value = """
        select s.fk_user_id as userId,
               coalesce(sum(
                   extract(epoch from (
                       least(coalesce(s.ended_at, cast(:now as timestamp)), cast(:endOfDay as timestamp)) -
                       greatest(s.started_at, cast(:startOfDay as timestamp))
                   ))
               ), 0) as totalSeconds
        from study_sessions s
        where s.fk_user_id IN :userIds
            and s.started_at < :endOfDay
            and (s.ended_at is null or s.ended_at > :startOfDay)
        group by s.fk_user_id
    """, nativeQuery = true)
    List<UserStudyTimeProjection> getTotalStudyTimeInSecondsByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            @Param("now") LocalDateTime now
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
        from study_sessions ss
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
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}