package com.process.clash.adapter.persistence.record.v2.subject;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordSubjectV2JpaRepository extends JpaRepository<RecordSubjectV2JpaEntity, Long> {

    @EntityGraph(attributePaths = {"user"})
    List<RecordSubjectV2JpaEntity> findAllByUserIdOrderByCreatedAtAsc(Long userId);

    @EntityGraph(attributePaths = {"user"})
    Optional<RecordSubjectV2JpaEntity> findByIdAndUserId(Long id, Long userId);
}
