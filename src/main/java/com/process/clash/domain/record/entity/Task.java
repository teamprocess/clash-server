package com.process.clash.domain.record.entity;

import com.process.clash.domain.user.user.entity.User;
import java.time.LocalDateTime;

public record Task(
    Long id,
    String name,
    Long studyTime, // ms
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    User user
) {
    public static Task create(String name, User user) {
        return new Task(
            null,
            name,
            0L,
            LocalDateTime.now(),
            LocalDateTime.now(),
            user
        );
    }
}
