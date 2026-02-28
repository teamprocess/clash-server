package com.process.clash.application.record.v2.port.out;

import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.v2.entity.RecordDevelopSessionSegmentV2;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RecordDevelopSessionSegmentV2RepositoryPort {

    record AppActivityTotal(
        MonitoredApp appId,
        Long totalStudyTimeSeconds
    ) {
    }

    RecordDevelopSessionSegmentV2 save(RecordDevelopSessionSegmentV2 segment);

    Optional<RecordDevelopSessionSegmentV2> findOpenSegmentBySessionId(Long sessionId);

    Optional<RecordDevelopSessionSegmentV2> findOpenSegmentBySessionIdForUpdate(Long sessionId);

    List<AppActivityTotal> findAppActivityTotalsByUserIdAndRange(
        Long userId,
        Instant startTime,
        Instant endTime,
        Instant now
    );
}
