package com.process.clash.domain.announcement.entity;

import java.time.Instant;

public record Announcement(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        String title,
        String author,
        Long userId,
        String content,
        Instant startAt,
        Instant endAt,
        boolean hideable
) {
}
