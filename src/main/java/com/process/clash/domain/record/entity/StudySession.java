package com.process.clash.domain.record.entity;

import com.process.clash.domain.user.user.entity.User;

import java.time.LocalDateTime;

public record StudySession (
    Long id,
    User user,
    Task task,
    LocalDateTime startedAt,
    LocalDateTime endedAt
) {
    public static StudySession create(Long id, User user, Task task, LocalDateTime startedAt, LocalDateTime endedAt) {
        return new StudySession(
            id,
            user,
            task,
            startedAt,
            endedAt
        );
    }

    public StudySession changeEndedAt(LocalDateTime newEndedAt) {
        return new StudySession(
                this.id,
                this.user,
                this.task,
                this.startedAt,
                newEndedAt
        );
    }
}
