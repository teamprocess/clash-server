package com.process.clash.adapter.persistence.record.v2.segment;

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
public interface RecordDevelopSessionSegmentV2JpaRepository
    extends JpaRepository<RecordDevelopSessionSegmentV2JpaEntity, Long> {

    interface AppActivityTotalProjection {
        String getAppId();
        Long getTotalSeconds();
    }

    Optional<RecordDevelopSessionSegmentV2JpaEntity> findByDevelopSession_IdAndEndedAtIsNull(Long sessionId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select s
        from RecordDevelopSessionSegmentV2JpaEntity s
        where s.developSession.id = :sessionId
          and s.endedAt is null
    """)
    Optional<RecordDevelopSessionSegmentV2JpaEntity> findOpenBySessionIdForUpdate(@Param("sessionId") Long sessionId);

    @Query(value = """
        with clamped_segments as (
            select
                s.app_id as app_id,
                least(coalesce(s.ended_at, :now), :endTime) as clamped_end,
                greatest(s.started_at, :startTime) as clamped_start
            from record_develop_session_segments_v2 s
            join record_develop_sessions_v2 ds
                on ds.id = s.fk_record_develop_session_id
            join record_active_sessions_v2 asv
                on asv.id = ds.id
            where asv.fk_user_id = :userId
                and s.started_at < :endTime
                and coalesce(s.ended_at, :now) > :startTime
        )
        select
            app_id as appId,
            cast(
                coalesce(
                    sum(
                        extract(epoch from (clamped_end - clamped_start))
                    ),
                    0
                ) as bigint
            ) as totalSeconds
        from clamped_segments
        group by app_id
        order by totalSeconds desc, app_id asc
    """, nativeQuery = true)
    List<AppActivityTotalProjection> findAppActivityTotalsByUserIdAndRange(
        @Param("userId") Long userId,
        @Param("startTime") Instant startTime,
        @Param("endTime") Instant endTime,
        @Param("now") Instant now
    );
}
