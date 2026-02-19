package com.process.clash.adapter.web.record.dto;

import com.process.clash.application.record.data.RecordSessionData;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.enums.RecordType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public class RecordSessionDto {

    @Schema(name = "RecordSessionDto")
    public record Session(
        Long id,
        RecordType recordType,
        Instant startedAt,
        Instant endedAt,
        Task task,
        Activity activity
    ) {
        public static Session from(RecordSessionData.Session session) {
            if (session == null) {
                return null;
            }
            return new Session(
                session.id(),
                session.recordType(),
                session.startedAt(),
                session.endedAt(),
                session.task() == null ? null : Task.from(session.task()),
                session.activity() == null ? null : Activity.from(session.activity())
            );
        }
    }

    public record Task(
        Long id,
        String name
    ) {
        public static Task from(RecordSessionData.Task task) {
            return new Task(task.id(), task.name());
        }
    }

    public record Activity(
        MonitoredApp appId
    ) {
        public static Activity from(RecordSessionData.Activity activity) {
            return new Activity(activity.appId());
        }
    }
}
