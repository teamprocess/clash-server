package com.process.clash.adapter.persistence.record.v2.task;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordTaskV2JpaRepository extends JpaRepository<RecordTaskV2JpaEntity, Long> {

    @EntityGraph(attributePaths = {"subject", "subject.user"})
    List<RecordTaskV2JpaEntity> findAllBySubjectIdInOrderByCreatedAtAsc(Collection<Long> subjectIds);

    @EntityGraph(attributePaths = {"subject", "subject.user"})
    Optional<RecordTaskV2JpaEntity> findByIdAndSubjectId(Long id, Long subjectId);
}
