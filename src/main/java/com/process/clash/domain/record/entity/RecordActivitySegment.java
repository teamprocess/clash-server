package com.process.clash.domain.record.entity;

import java.time.LocalDateTime;

public record RecordActivitySegment(
    Long id,
    Long sessionId,
    String appName,
    LocalDateTime startedAt,
    LocalDateTime endedAt
) {

    public static RecordActivitySegment start(Long sessionId, String appName, LocalDateTime startedAt) {
        return new RecordActivitySegment(
            null,
            sessionId,
            appName,
            startedAt,
            null
        );
    }

    public RecordActivitySegment changeEndedAt(LocalDateTime endedAt) {
        return new RecordActivitySegment(
            this.id,
            this.sessionId,
            this.appName,
            this.startedAt,
            endedAt
        );
    }
}
