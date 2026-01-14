package com.process.clash.adapter.persistence.studysession;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.record.model.entity.StudySession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudySessionJpaRepository extends JpaRepository<StudySessionJpaEntity, Long> {

    @EntityGraph(attributePaths = {"user", "task"})
    List<StudySessionJpaEntity> findAllByUserId(Long userId);

    Boolean existsByUserIdAndEndedAtIsNull(Long userId);
    Optional<StudySession> findByUserIdAndEndedAtIsNull(Long userId);

    Long user(UserJpaEntity user);
}
