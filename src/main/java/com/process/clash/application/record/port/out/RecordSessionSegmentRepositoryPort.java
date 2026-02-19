package com.process.clash.application.record.port.out;

import com.process.clash.domain.record.entity.RecordSessionSegment;
import java.util.Optional;

public interface RecordSessionSegmentRepositoryPort {

    RecordSessionSegment save(RecordSessionSegment segment);

    Optional<RecordSessionSegment> findOpenSegmentBySessionId(Long sessionId);

    Optional<RecordSessionSegment> findOpenSegmentBySessionIdForUpdate(Long sessionId);
}
