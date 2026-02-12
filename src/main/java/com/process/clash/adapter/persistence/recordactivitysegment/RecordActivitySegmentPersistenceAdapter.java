package com.process.clash.adapter.persistence.recordactivitysegment;

import com.process.clash.adapter.persistence.studysession.StudySessionJpaEntity;
import com.process.clash.adapter.persistence.studysession.StudySessionJpaRepository;
import com.process.clash.application.record.port.out.RecordActivitySegmentRepositoryPort;
import com.process.clash.domain.record.entity.RecordActivitySegment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RecordActivitySegmentPersistenceAdapter implements RecordActivitySegmentRepositoryPort {

    private final RecordActivitySegmentJpaRepository recordActivitySegmentJpaRepository;
    private final RecordActivitySegmentJpaMapper recordActivitySegmentJpaMapper;
    private final StudySessionJpaRepository studySessionJpaRepository;

    @Override
    public RecordActivitySegment save(RecordActivitySegment segment) {
        if (segment.id() == null) {
            StudySessionJpaEntity session = studySessionJpaRepository.getReferenceById(segment.sessionId());
            RecordActivitySegmentJpaEntity entity = recordActivitySegmentJpaMapper.toJpaEntity(segment, session);
            RecordActivitySegmentJpaEntity saved = recordActivitySegmentJpaRepository.save(entity);
            return recordActivitySegmentJpaMapper.toDomain(saved);
        }

        RecordActivitySegmentJpaEntity existing = recordActivitySegmentJpaRepository.findById(segment.id())
            .orElseThrow(() -> new IllegalStateException(
                "RecordActivitySegment not found with id: " + segment.id()
            ));
        existing.changeEndedAt(segment.endedAt());
        RecordActivitySegmentJpaEntity saved = recordActivitySegmentJpaRepository.save(existing);
        return recordActivitySegmentJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<RecordActivitySegment> findOpenSegmentBySessionId(Long sessionId) {
        return recordActivitySegmentJpaRepository.findBySession_IdAndEndedAtIsNull(sessionId)
            .map(recordActivitySegmentJpaMapper::toDomain);
    }

    @Override
    public Optional<RecordActivitySegment> findOpenSegmentBySessionIdForUpdate(Long sessionId) {
        return recordActivitySegmentJpaRepository.findOpenBySessionIdForUpdate(sessionId)
            .map(recordActivitySegmentJpaMapper::toDomain);
    }
}
