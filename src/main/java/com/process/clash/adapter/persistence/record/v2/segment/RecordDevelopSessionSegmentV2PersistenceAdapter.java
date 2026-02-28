package com.process.clash.adapter.persistence.record.v2.segment;

import com.process.clash.adapter.persistence.record.v2.session.RecordDevelopSessionV2JpaEntity;
import com.process.clash.adapter.persistence.record.v2.session.RecordDevelopSessionV2JpaRepository;
import com.process.clash.application.record.v2.exception.exception.notfound.RecordDevelopSegmentV2NotFoundException;
import com.process.clash.application.record.v2.port.out.RecordDevelopSessionSegmentV2RepositoryPort;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.v2.entity.RecordDevelopSessionSegmentV2;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecordDevelopSessionSegmentV2PersistenceAdapter implements RecordDevelopSessionSegmentV2RepositoryPort {

    private final RecordDevelopSessionSegmentV2JpaRepository recordDevelopSessionSegmentV2JpaRepository;
    private final RecordDevelopSessionV2JpaRepository recordDevelopSessionV2JpaRepository;
    private final RecordDevelopSessionSegmentV2JpaMapper recordDevelopSessionSegmentV2JpaMapper;

    @Override
    public RecordDevelopSessionSegmentV2 save(RecordDevelopSessionSegmentV2 segment) {
        if (segment.id() == null) {
            RecordDevelopSessionV2JpaEntity session = recordDevelopSessionV2JpaRepository
                .getReferenceById(segment.sessionId());
            RecordDevelopSessionSegmentV2JpaEntity entity =
                recordDevelopSessionSegmentV2JpaMapper.toJpaEntity(segment, session);
            return recordDevelopSessionSegmentV2JpaMapper.toDomain(
                recordDevelopSessionSegmentV2JpaRepository.save(entity)
            );
        }

        RecordDevelopSessionSegmentV2JpaEntity existing = recordDevelopSessionSegmentV2JpaRepository
            .findById(segment.id())
            .orElseThrow(RecordDevelopSegmentV2NotFoundException::new);
        existing.changeEndedAt(segment.endedAt());
        return recordDevelopSessionSegmentV2JpaMapper.toDomain(
            recordDevelopSessionSegmentV2JpaRepository.saveAndFlush(existing)
        );
    }

    @Override
    public Optional<RecordDevelopSessionSegmentV2> findOpenSegmentBySessionId(Long sessionId) {
        return recordDevelopSessionSegmentV2JpaRepository.findByDevelopSession_IdAndEndedAtIsNull(sessionId)
            .map(recordDevelopSessionSegmentV2JpaMapper::toDomain);
    }

    @Override
    public Optional<RecordDevelopSessionSegmentV2> findOpenSegmentBySessionIdForUpdate(Long sessionId) {
        return recordDevelopSessionSegmentV2JpaRepository.findOpenBySessionIdForUpdate(sessionId)
            .map(recordDevelopSessionSegmentV2JpaMapper::toDomain);
    }

    @Override
    public List<AppActivityTotal> findAppActivityTotalsByUserIdAndRange(
        Long userId,
        Instant startTime,
        Instant endTime,
        Instant now
    ) {
        return recordDevelopSessionSegmentV2JpaRepository.findAppActivityTotalsByUserIdAndRange(
                userId,
                startTime,
                endTime,
                now
            ).stream()
            .map(row -> new AppActivityTotal(
                MonitoredApp.valueOf(row.getAppId()),
                row.getTotalSeconds()
            ))
            .toList();
    }
}
