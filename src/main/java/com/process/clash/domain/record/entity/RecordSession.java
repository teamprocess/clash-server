package com.process.clash.domain.record.entity;

import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.enums.RecordType;
import com.process.clash.domain.user.user.entity.User;

import java.time.Instant;

public record RecordSession (
    Long id,
    User user,
    RecordTask task,
    RecordType recordType,
    MonitoredApp appId,
    Instant startedAt,
    Instant endedAt
) {
    public static RecordSession create(
        Long id,
        User user,
        RecordTask task,
        RecordType recordType,
        MonitoredApp appId,
        Instant startedAt,
        Instant endedAt
    ) {
        return new RecordSession(
            id,
            user,
            task,
            recordType,
            appId,
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
        MonitoredApp appId,
        Instant startedAt,
        Instant endedAt
    ) {
        return create(
            id,
            user,
            null,
            RecordType.ACTIVITY,
            appId,
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
                this.appId,
                this.startedAt,
                newEndedAt
        );
    }

    public RecordSession changeActivityAppId(MonitoredApp newAppId) {
        return new RecordSession(
            this.id,
            this.user,
            this.task,
            this.recordType,
            newAppId,
            this.startedAt,
            this.endedAt
        );
    }
}
