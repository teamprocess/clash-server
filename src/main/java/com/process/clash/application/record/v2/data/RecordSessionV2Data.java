package com.process.clash.application.record.v2.data;

import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import java.time.Instant;

public class RecordSessionV2Data {

    public record Session(
        Long id,
        RecordSessionTypeV2 sessionType,
        Instant startedAt,
        Instant endedAt,
        Subject subject,
        Task task,
        Develop develop
    ) {
        public static Session task(
            Long id,
            Instant startedAt,
            Instant endedAt,
            Long subjectId,
            String subjectName,
            Long taskId,
            String taskName
        ) {
            return new Session(
                id,
                RecordSessionTypeV2.TASK,
                startedAt,
                endedAt,
                new Subject(subjectId, subjectName),
                taskId == null ? null : new Task(taskId, taskName),
                null
            );
        }

        public static Session develop(
            Long id,
            Instant startedAt,
            Instant endedAt,
            MonitoredApp appId
        ) {
            return new Session(
                id,
                RecordSessionTypeV2.DEVELOP,
                startedAt,
                endedAt,
                null,
                null,
                new Develop(appId)
            );
        }
    }

    public record Subject(
        Long id,
        String name
    ) {
    }

    public record Task(
        Long id,
        String name
    ) {
    }

    public record Develop(
        MonitoredApp appId
    ) {
    }
}
