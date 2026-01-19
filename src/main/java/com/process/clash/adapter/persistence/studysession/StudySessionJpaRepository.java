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
                least(coalesce(s.endedAt, current_timestamp), cast(:endOfDay as timestamp)) - greatest(s.startedAt, cast(:startOfDay as timestamp))
            ))
        ), 0)
        from StudySessionJpaEntity s
        where s.user.id = :userId
            and s.startedAt < :endOfDay
            and (s.endedAt is null or s.endedAt > :startOfDay)
    """, nativeQuery = true)
    Long getTotalStudyTimeInSeconds(
            @Param("userId") Long userId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}
