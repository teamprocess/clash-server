package com.process.clash.domain.helpcontent.entity;

import java.time.Instant;

public record HelpContent(
        String key,
        String content,
        long version,
        Instant createdAt,
        Instant updatedAt
) {
}
