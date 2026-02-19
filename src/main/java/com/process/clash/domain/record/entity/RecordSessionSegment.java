package com.process.clash.domain.record.entity;

import com.process.clash.domain.record.enums.MonitoredApp;
import java.time.Instant;

public record RecordSessionSegment(
    Long id,
    Long sessionId,
    MonitoredApp appId,
    Instant startedAt,
    Instant endedAt
) {

    public static RecordSessionSegment start(Long sessionId, MonitoredApp appId, Instant startedAt) {
        return new RecordSessionSegment(
            null,
            sessionId,
            appId,
            startedAt,
            null
        );
    }

    public RecordSessionSegment changeEndedAt(Instant endedAt) {
        return new RecordSessionSegment(
            this.id,
            this.sessionId,
            this.appId,
            this.startedAt,
            endedAt
        );
    }
}
