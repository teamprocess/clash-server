package com.process.clash.adapter.persistence.recordsessionsegment;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordSessionSegmentJpaRepository extends JpaRepository<RecordSessionSegmentJpaEntity, Long> {

    Optional<RecordSessionSegmentJpaEntity> findBySession_IdAndEndedAtIsNull(Long sessionId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select s
        from RecordSessionSegmentJpaEntity s
        where s.session.id = :sessionId
          and s.endedAt is null
    """)
    Optional<RecordSessionSegmentJpaEntity> findOpenBySessionIdForUpdate(@Param("sessionId") Long sessionId);
}
