package com.process.clash.domain.record.entity;

import java.time.Instant;

public record RecordSessionSegment(
    Long id,
    Long sessionId,
    String appName,
    Instant startedAt,
    Instant endedAt
) {

    public static RecordSessionSegment start(Long sessionId, String appName, Instant startedAt) {
        return new RecordSessionSegment(
            null,
            sessionId,
            appName,
            startedAt,
            null
        );
    }

    public RecordSessionSegment changeEndedAt(Instant endedAt) {
        return new RecordSessionSegment(
            this.id,
            this.sessionId,
            this.appName,
            this.startedAt,
            endedAt
        );
    }
}
