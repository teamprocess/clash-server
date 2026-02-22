package com.process.clash.application.record.v2.port.out;

import com.process.clash.domain.record.v2.entity.RecordDevelopSessionSegmentV2;
import java.util.Optional;

public interface RecordDevelopSessionSegmentV2RepositoryPort {

    RecordDevelopSessionSegmentV2 save(RecordDevelopSessionSegmentV2 segment);

    Optional<RecordDevelopSessionSegmentV2> findOpenSegmentBySessionId(Long sessionId);

    Optional<RecordDevelopSessionSegmentV2> findOpenSegmentBySessionIdForUpdate(Long sessionId);
}
