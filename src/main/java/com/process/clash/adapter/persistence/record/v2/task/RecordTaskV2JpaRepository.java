package com.process.clash.adapter.persistence.record.v2.task;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordTaskV2JpaRepository extends JpaRepository<RecordTaskV2JpaEntity, Long> {

    @EntityGraph(attributePaths = {"subject", "user"})
    List<RecordTaskV2JpaEntity> findAllBySubjectIdInOrderByCreatedAtAsc(Collection<Long> subjectIds);

    @EntityGraph(attributePaths = {"subject", "user"})
    Optional<RecordTaskV2JpaEntity> findByIdAndSubjectId(Long id, Long subjectId);

    @EntityGraph(attributePaths = {"subject", "user"})
    Optional<RecordTaskV2JpaEntity> findByIdAndUserId(Long id, Long userId);

    @EntityGraph(attributePaths = {"subject", "user"})
    Optional<RecordTaskV2JpaEntity> findByIdAndSubjectIdAndUserId(Long id, Long subjectId, Long userId);

    @EntityGraph(attributePaths = {"subject", "user"})
    @Query("""
        select t
        from RecordTaskV2JpaEntity t
        left join t.subject s
        where t.user.id = :userId
        order by case when s.id is null then 0 else 1 end asc, s.id desc, t.createdAt asc
    """)
    List<RecordTaskV2JpaEntity> findAllByUserIdOrderBySubjectIdDescNullsFirst(@Param("userId") Long userId);
}
