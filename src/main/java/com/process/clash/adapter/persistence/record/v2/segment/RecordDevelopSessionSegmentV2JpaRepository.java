package com.process.clash.adapter.persistence.record.v2.segment;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordDevelopSessionSegmentV2JpaRepository
    extends JpaRepository<RecordDevelopSessionSegmentV2JpaEntity, Long> {

    Optional<RecordDevelopSessionSegmentV2JpaEntity> findByDevelopSession_IdAndEndedAtIsNull(Long sessionId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select s
        from RecordDevelopSessionSegmentV2JpaEntity s
        where s.developSession.id = :sessionId
          and s.endedAt is null
    """)
    Optional<RecordDevelopSessionSegmentV2JpaEntity> findOpenBySessionIdForUpdate(@Param("sessionId") Long sessionId);
}
