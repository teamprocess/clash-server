package com.process.clash.adapter.persistence.studysession;

import com.process.clash.domain.record.model.entity.StudySession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    @EntityGraph(attributePaths = {"user", "task"})
    Optional<StudySessionJpaEntity> findByUserIdAndEndedAtIsNull(Long userId);

    List<StudySession> findByUserIdAndStartedAtAfter(Long userId, LocalDateTime startedAtAfter);

    @Query(value = """
        select coalesce(sum(
            extract(epoch from (
                least(coalesce(s.ended_at, current_timestamp), cast(:endOfDay as timestamp)) - 
                greatest(s.started_at, cast(:startOfDay as timestamp))
            ))
        ), 0)
        from study_session s
        where s.user_id = :userId
            and s.started_at < :endOfDay
            and (s.ended_at is null or s.ended_at > :startOfDay)
    """, nativeQuery = true)
    Long getTotalStudyTimeInSeconds(
            @Param("userId") Long userId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    @Query(value = """
        select s.user_id as userId,
               coalesce(sum(
                   extract(epoch from (
                       least(coalesce(s.ended_at, current_timestamp), cast(:endOfDay as timestamp)) - 
                       greatest(s.started_at, cast(:startOfDay as timestamp))
                   ))
               ), 0) as totalSeconds
        from study_session s
        where s.user_id IN :userIds
            and s.started_at < :endOfDay
            and (s.ended_at is null or s.ended_at > :startOfDay)
        group by s.user_id
    """, nativeQuery = true)
    List<UserStudyTimeProjection> getTotalStudyTimeInSecondsByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    interface UserStudyTimeProjection {
        Long getUserId();
        Long getTotalSeconds();
    }
}