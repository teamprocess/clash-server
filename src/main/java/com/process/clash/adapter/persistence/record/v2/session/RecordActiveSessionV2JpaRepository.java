package com.process.clash.adapter.persistence.record.v2.session;

import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordActiveSessionV2JpaRepository extends JpaRepository<RecordActiveSessionV2JpaEntity, Long> {

    Boolean existsByUserIdAndEndedAtIsNull(Long userId);

    @Query("""
        select s
        from RecordActiveSessionV2JpaEntity s
        join fetch s.user
        left join fetch s.developSession ds
        left join fetch s.taskSession ts
        left join fetch ts.subject sub
        left join fetch ts.task task
        where s.id = :sessionId
    """)
    Optional<RecordActiveSessionV2JpaEntity> findByIdWithDetails(@Param("sessionId") Long sessionId);

    @Query("""
        select s
        from RecordActiveSessionV2JpaEntity s
        join fetch s.user
        left join fetch s.developSession ds
        left join fetch s.taskSession ts
        left join fetch ts.subject sub
        left join fetch ts.task task
        where s.user.id = :userId
          and s.endedAt is null
    """)
    Optional<RecordActiveSessionV2JpaEntity> findActiveByUserId(@Param("userId") Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select s
        from RecordActiveSessionV2JpaEntity s
        join fetch s.user
        left join fetch s.developSession ds
        left join fetch s.taskSession ts
        left join fetch ts.subject sub
        left join fetch ts.task task
        where s.user.id = :userId
          and s.endedAt is null
    """)
    Optional<RecordActiveSessionV2JpaEntity> findActiveByUserIdForUpdate(@Param("userId") Long userId);

    @Query("""
        select s
        from RecordActiveSessionV2JpaEntity s
        join fetch s.user
        left join fetch s.developSession ds
        left join fetch s.taskSession ts
        left join fetch ts.subject sub
        left join fetch ts.task task
        where s.user.id = :userId
          and s.startedAt < :endTime
          and (s.endedAt is null or s.endedAt > :startTime)
        order by s.startedAt asc
    """)
    List<RecordActiveSessionV2JpaEntity> findAllOverlappingByUserId(
        @Param("userId") Long userId,
        @Param("startTime") Instant startTime,
        @Param("endTime") Instant endTime
    );
}
