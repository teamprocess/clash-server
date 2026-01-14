package com.process.clash.domain.record.model.entity;

import com.process.clash.domain.user.user.entity.User;

import java.time.LocalDateTime;

public record Session (
    Long id,
    User user,
    Task task,
    LocalDateTime startedAt,
    LocalDateTime endedAt
) {
    public static Session create(Long id, User user, Task task, LocalDateTime startedAt, LocalDateTime endedAt) {
        return new Session(
            id,
            user,
            task,
            startedAt,
            endedAt
        );
    }
}
