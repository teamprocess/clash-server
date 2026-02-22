package com.process.clash.adapter.persistence.record.v2.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordTaskSessionV2JpaRepository extends JpaRepository<RecordTaskSessionV2JpaEntity, Long> {

    @Query("""
        select case when count(ts) > 0 then true else false end
        from RecordTaskSessionV2JpaEntity ts
        join ts.activeSession s
        where ts.subject.id = :subjectId
          and s.endedAt is null
    """)
    boolean existsActiveBySubjectId(@Param("subjectId") Long subjectId);

    @Query("""
        select case when count(ts) > 0 then true else false end
        from RecordTaskSessionV2JpaEntity ts
        join ts.activeSession s
        where ts.task.id = :taskId
          and s.endedAt is null
    """)
    boolean existsActiveByTaskId(@Param("taskId") Long taskId);
}
