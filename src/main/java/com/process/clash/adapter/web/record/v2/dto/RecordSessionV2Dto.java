package com.process.clash.adapter.web.record.v2.dto;

import com.process.clash.application.record.v2.data.RecordSessionV2Data;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public class RecordSessionV2Dto {

    @Schema(name = "RecordSessionV2Dto")
    public record Session(
        Long id,
        RecordSessionTypeV2 sessionType,
        Instant startedAt,
        Instant endedAt,
        Subject subject,
        Task task,
        Develop develop
    ) {
        public static Session from(RecordSessionV2Data.Session session) {
            if (session == null) {
                return null;
            }
            return new Session(
                session.id(),
                session.sessionType(),
                session.startedAt(),
                session.endedAt(),
                session.subject() == null ? null : Subject.from(session.subject()),
                session.task() == null ? null : Task.from(session.task()),
                session.develop() == null ? null : Develop.from(session.develop())
            );
        }
    }

    public record Subject(
        Long id,
        String name
    ) {
        public static Subject from(RecordSessionV2Data.Subject subject) {
            return new Subject(subject.id(), subject.name());
        }
    }

    public record Task(
        Long id,
        String name
    ) {
        public static Task from(RecordSessionV2Data.Task task) {
            return new Task(task.id(), task.name());
        }
    }

    public record Develop(
        MonitoredApp appId
    ) {
        public static Develop from(RecordSessionV2Data.Develop develop) {
            return new Develop(develop.appId());
        }
    }
}
