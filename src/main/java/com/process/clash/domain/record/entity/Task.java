package com.process.clash.domain.record.entity;

import com.process.clash.domain.user.user.entity.User;
import java.time.Instant;

public record Task(
    Long id,
    String name,
    Long studyTime, // s
    Instant createdAt,
    Instant updatedAt,
    User user
) {
    public static Task create(String name, User user) {
        return new Task(
            null,
            name,
            0L,
            Instant.now(),
            Instant.now(),
            user
        );
    }
}
