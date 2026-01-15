package com.process.clash.application.record.dto;

import com.process.clash.application.common.actor.Actor;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public class GetTodayRecordData {

    @Builder
    public record Command(
        Actor actor
    ) {}

    @Builder
    public record Result(
        String date,
        Boolean pomodoroEnabled,
        Long totalStudyTime,
        Instant studyStoppedAt,
        List<Session> sessions
    ) {

        public static Result create(
            String date,
            Boolean pomodoroEnabled,
            Long totalStudyTime,
            Instant studyStoppedAt,
            List<Session> sessions
        ) {
            return new Result(
                date,
                pomodoroEnabled,
                totalStudyTime,
                studyStoppedAt,
                sessions
            );
        }
    }

    public record Session(
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        Long taskId,
        String taskName
    ) {
        public static Session from(LocalDateTime startedAt, LocalDateTime endedAt, Long taskId, String taskName) {
            return new Session(startedAt, endedAt, taskId, taskName);
        }
    }
}
