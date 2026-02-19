package com.process.clash.adapter.persistence.recordsessionsegment;

import com.process.clash.adapter.persistence.recordsession.RecordSessionJpaEntity;
import com.process.clash.adapter.persistence.recordsession.RecordSessionJpaRepository;
import com.process.clash.application.record.exception.exception.notfound.RecordSessionSegmentNotFound;
import com.process.clash.application.record.port.out.RecordSessionSegmentRepositoryPort;
import com.process.clash.domain.record.entity.RecordSessionSegment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RecordSessionSegmentPersistenceAdapter implements RecordSessionSegmentRepositoryPort {

    private final RecordSessionSegmentJpaRepository recordSessionSegmentJpaRepository;
    private final RecordSessionSegmentJpaMapper recordSessionSegmentJpaMapper;
    private final RecordSessionJpaRepository recordSessionJpaRepository;

    @Override
    public RecordSessionSegment save(RecordSessionSegment segment) {
        if (segment.id() == null) {
            RecordSessionJpaEntity session = recordSessionJpaRepository.getReferenceById(segment.sessionId());
            RecordSessionSegmentJpaEntity entity = recordSessionSegmentJpaMapper.toJpaEntity(segment, session);
            RecordSessionSegmentJpaEntity saved = recordSessionSegmentJpaRepository.save(entity);
            return recordSessionSegmentJpaMapper.toDomain(saved);
        }

        RecordSessionSegmentJpaEntity existing = recordSessionSegmentJpaRepository.findById(segment.id())
            .orElseThrow(RecordSessionSegmentNotFound::new);
        existing.changeEndedAt(segment.endedAt());
        // 같은 트랜잭션에서 close -> new open segment 순서를 보장하기 위해 즉시 flush
        RecordSessionSegmentJpaEntity saved = recordSessionSegmentJpaRepository.saveAndFlush(existing);
        return recordSessionSegmentJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<RecordSessionSegment> findOpenSegmentBySessionId(Long sessionId) {
        return recordSessionSegmentJpaRepository.findBySession_IdAndEndedAtIsNull(sessionId)
            .map(recordSessionSegmentJpaMapper::toDomain);
    }

    @Override
    public Optional<RecordSessionSegment> findOpenSegmentBySessionIdForUpdate(Long sessionId) {
        return recordSessionSegmentJpaRepository.findOpenBySessionIdForUpdate(sessionId)
            .map(recordSessionSegmentJpaMapper::toDomain);
    }
}
