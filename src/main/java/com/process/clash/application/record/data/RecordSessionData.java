package com.process.clash.application.record.data;

import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.enums.RecordType;
import java.time.Instant;

public class RecordSessionData {

    public record Session(
        Long id,
        RecordType recordType,
        Instant startedAt,
        Instant endedAt,
        Task task,
        Activity activity
    ) {
        public static Session task(
            Long id,
            Instant startedAt,
            Instant endedAt,
            Long taskId,
            String taskName
        ) {
            return new Session(
                id,
                RecordType.TASK,
                startedAt,
                endedAt,
                new Task(taskId, taskName),
                null
            );
        }

        public static Session activity(
            Long id,
            Instant startedAt,
            Instant endedAt,
            MonitoredApp appId
        ) {
            return new Session(
                id,
                RecordType.ACTIVITY,
                startedAt,
                endedAt,
                null,
                new Activity(appId)
            );
        }
    }

    public record Task(
        Long id,
        String name
    ) {}

    public record Activity(
        MonitoredApp appId
    ) {}
}
