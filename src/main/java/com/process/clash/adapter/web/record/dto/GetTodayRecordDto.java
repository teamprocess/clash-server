package com.process.clash.adapter.web.record.dto;

import com.process.clash.application.record.data.GetTodayRecordData;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public class GetTodayRecordDto {

    @Schema(name = "GetTodayRecordDtoResponse")

    public record Response(
        String date,
        Boolean pomodoroEnabled,
        Long totalStudyTime,
        Instant studyStoppedAt,
        List<Session> sessions
    ) {
        public static Response from(GetTodayRecordData.Result result){
            return new Response(
                result.date(),
                result.pomodoroEnabled(),
                result.totalStudyTime(),
                result.studyStoppedAt(),
                result.sessions().stream()
                    .map(Session::from)
                    .toList()
            );
        }
    }

    public record Session(
        Instant startedAt,
        Instant endedAt,
        Task task
    ) {
        public static Session from(GetTodayRecordData.Session session) {
            Instant endedAt = java.util.Optional.ofNullable(session.endedAt())
                .map(ldt -> ldt.atZone(ZoneOffset.UTC).toInstant())
                .orElse(null);
            return new Session(
                session.startedAt().atZone(ZoneOffset.UTC).toInstant(),
                endedAt,
                new Task(session.taskId(), session.taskName())
            );
        }
    }

    public record Task(
        Long id,
        String name
    ) {}
}
