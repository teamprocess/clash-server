package com.process.clash.application.realtime.data;

import java.time.Instant;

public record RefetchNotice(
    ChangeDomain domain,
    ChangeType type,
    Instant occurredAt
) {

    public static RefetchNotice of(ChangeDomain domain, ChangeType type) {
        return new RefetchNotice(domain, type, Instant.now());
    }

    public static RefetchNotice of(ChangeDomain domain, ChangeType type, Instant occurredAt) {
        return new RefetchNotice(domain, type, occurredAt == null ? Instant.now() : occurredAt);
    }
}
