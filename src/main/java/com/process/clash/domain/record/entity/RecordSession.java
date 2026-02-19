package com.process.clash.domain.record.entity;

import com.process.clash.domain.record.enums.RecordType;
import com.process.clash.domain.user.user.entity.User;

import java.time.Instant;

public record RecordSession (
    Long id,
    User user,
    RecordTask task,
    RecordType recordType,
    String appName,
    Instant startedAt,
    Instant endedAt
) {
    public static RecordSession create(
        Long id,
        User user,
        RecordTask task,
        RecordType recordType,
        String appName,
        Instant startedAt,
        Instant endedAt
    ) {
        return new RecordSession(
            id,
            user,
            task,
            recordType,
            appName,
            startedAt,
            endedAt
        );
    }

    public static RecordSession create(Long id, User user, RecordTask task, Instant startedAt, Instant endedAt) {
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

    public static RecordSession createActivity(
        Long id,
        User user,
        String appName,
        Instant startedAt,
        Instant endedAt
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

    public RecordSession changeEndedAt(Instant newEndedAt) {
        return new RecordSession(
                this.id,
                this.user,
                this.task,
                this.recordType,
                this.appName,
                this.startedAt,
                newEndedAt
        );
    }

    public RecordSession changeActivityAppName(String newAppName) {
        return new RecordSession(
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
