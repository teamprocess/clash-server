package com.process.clash.application.record.port.out;

import com.process.clash.domain.record.entity.RecordActivitySegment;
import java.util.Optional;

public interface RecordActivitySegmentRepositoryPort {

    RecordActivitySegment save(RecordActivitySegment segment);

    Optional<RecordActivitySegment> findOpenSegmentBySessionId(Long sessionId);

    Optional<RecordActivitySegment> findOpenSegmentBySessionIdForUpdate(Long sessionId);
}
