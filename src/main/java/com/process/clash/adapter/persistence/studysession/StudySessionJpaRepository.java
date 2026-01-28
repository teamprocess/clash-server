package com.process.clash.adapter.persistence.studysession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.process.clash.application.ranking.data.UserRanking;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
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

    @EntityGraph(attributePaths = {"user", "task"})
    List<StudySessionJpaEntity> findAllByEndedAtIsNull();

    @Query("""
        select s
        from StudySessionJpaEntity s
        join fetch s.user
        join fetch s.task
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

    @Query("""
        select new com.process.clash.application.ranking.data.UserRanking(
                u.name,
                u.profileImage,
                case when count(r) > 0 then true else false end,
                u.username,
                cast(
                    coalesce(
                        sum(
                            function('date_part', 'epoch',
                                coalesce(ss.endedAt, current_timestamp) - ss.startedAt
                            )
                        ), 0
                    ) as long
                )
            )
        from StudySessionJpaEntity ss
        inner join ss.user u
        left join RivalJpaEntity r on
            (u.id in (r.firstUser.id, r.secondUser.id)
                and :userId in (r.firstUser.id, r.secondUser.id)
                and r.rivalLinkingStatus = 'ACCEPTED')
        where ss.startedAt >= :startDate
            and ss.startedAt < :endDate
        group by u.id, u.name, u.profileImage, u.username
        order by sum(
            function('date_part', 'epoch',
                coalesce(ss.endedAt, current_timestamp) - ss.startedAt
            )
        ) desc
    """)
    List<UserRanking> findStudyTimeRankingByUserIdAndPeriod(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
