package com.process.clash.domain.record.entity;

import java.time.Instant;

public record RecordActivitySegment(
    Long id,
    Long sessionId,
    String appName,
    Instant startedAt,
    Instant endedAt
) {

    public static RecordActivitySegment start(Long sessionId, String appName, Instant startedAt) {
        return new RecordActivitySegment(
            null,
            sessionId,
            appName,
            startedAt,
            null
        );
    }

    public RecordActivitySegment changeEndedAt(Instant endedAt) {
        return new RecordActivitySegment(
            this.id,
            this.sessionId,
            this.appName,
            this.startedAt,
            endedAt
        );
    }
}
