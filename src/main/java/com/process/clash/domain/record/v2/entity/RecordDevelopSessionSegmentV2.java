package com.process.clash.domain.record.v2.entity;

import com.process.clash.domain.record.enums.MonitoredApp;
import java.time.Instant;

public record RecordDevelopSessionSegmentV2(
    Long id,
    Long sessionId,
    MonitoredApp appId,
    Instant startedAt,
    Instant endedAt
) {

    public static RecordDevelopSessionSegmentV2 start(
        Long sessionId,
        MonitoredApp appId,
        Instant startedAt
    ) {
        return new RecordDevelopSessionSegmentV2(
            null,
            sessionId,
            appId,
            startedAt,
            null
        );
    }

    public RecordDevelopSessionSegmentV2 changeEndedAt(Instant endedAt) {
        return new RecordDevelopSessionSegmentV2(
            this.id,
            this.sessionId,
            this.appId,
            this.startedAt,
            endedAt
        );
    }
}
