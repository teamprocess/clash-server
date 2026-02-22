package com.process.clash.domain.record.v2.entity;

import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import java.time.Instant;

public record RecordSessionV2(
    Long id,
    Long userId,
    RecordSessionTypeV2 sessionType,
    Long subjectId,
    String subjectName,
    Long taskId,
    String taskName,
    MonitoredApp appId,
    Instant startedAt,
    Instant endedAt
) {

    public static RecordSessionV2 createTask(
        Long userId,
        Long subjectId,
        String subjectName,
        Long taskId,
        String taskName,
        Instant startedAt
    ) {
        return new RecordSessionV2(
            null,
            userId,
            RecordSessionTypeV2.TASK,
            subjectId,
            subjectName,
            taskId,
            taskName,
            null,
            startedAt,
            null
        );
    }

    public static RecordSessionV2 createDevelop(
        Long userId,
        MonitoredApp appId,
        Instant startedAt
    ) {
        return new RecordSessionV2(
            null,
            userId,
            RecordSessionTypeV2.DEVELOP,
            null,
            null,
            null,
            null,
            appId,
            startedAt,
            null
        );
    }

    public RecordSessionV2 changeEndedAt(Instant endedAt) {
        return new RecordSessionV2(
            this.id,
            this.userId,
            this.sessionType,
            this.subjectId,
            this.subjectName,
            this.taskId,
            this.taskName,
            this.appId,
            this.startedAt,
            endedAt
        );
    }

    public RecordSessionV2 changeDevelopAppId(MonitoredApp appId) {
        return new RecordSessionV2(
            this.id,
            this.userId,
            this.sessionType,
            this.subjectId,
            this.subjectName,
            this.taskId,
            this.taskName,
            appId,
            this.startedAt,
            this.endedAt
        );
    }
}
