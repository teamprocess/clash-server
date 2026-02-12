package com.process.clash.domain.record.entity;

import com.process.clash.domain.record.enums.RecordType;
import com.process.clash.domain.user.user.entity.User;

import java.time.LocalDateTime;

public record StudySession (
    Long id,
    User user,
    Task task,
    RecordType recordType,
    String appName,
    LocalDateTime startedAt,
    LocalDateTime endedAt
) {
    public static StudySession create(
        Long id,
        User user,
        Task task,
        RecordType recordType,
        String appName,
        LocalDateTime startedAt,
        LocalDateTime endedAt
    ) {
        return new StudySession(
            id,
            user,
            task,
            recordType,
            appName,
            startedAt,
            endedAt
        );
    }

    public static StudySession create(Long id, User user, Task task, LocalDateTime startedAt, LocalDateTime endedAt) {
        return create(
            id,
            user,
            task,
            RecordType.TASK,
            null,
            startedAt,
            endedAt
        );
    }

    public static StudySession createActivity(
        Long id,
        User user,
        String appName,
        LocalDateTime startedAt,
        LocalDateTime endedAt
    ) {
        return create(
            id,
            user,
            null,
            RecordType.ACTIVITY,
            appName,
            startedAt,
            endedAt
        );
    }

    public StudySession changeEndedAt(LocalDateTime newEndedAt) {
        return new StudySession(
                this.id,
                this.user,
                this.task,
                this.recordType,
                this.appName,
                this.startedAt,
                newEndedAt
        );
    }

    public StudySession changeActivityAppName(String newAppName) {
        return new StudySession(
            this.id,
            this.user,
            this.task,
            this.recordType,
            newAppName,
            this.startedAt,
            this.endedAt
        );
    }
}
