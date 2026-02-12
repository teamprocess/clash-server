package com.process.clash.adapter.persistence.recordactivitysegment;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordActivitySegmentJpaRepository extends JpaRepository<RecordActivitySegmentJpaEntity, Long> {

    Optional<RecordActivitySegmentJpaEntity> findBySession_IdAndEndedAtIsNull(Long sessionId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select s
        from RecordActivitySegmentJpaEntity s
        where s.session.id = :sessionId
          and s.endedAt is null
    """)
    Optional<RecordActivitySegmentJpaEntity> findOpenBySessionIdForUpdate(@Param("sessionId") Long sessionId);
}
