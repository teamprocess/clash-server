package com.process.clash.application.record.data;

import com.process.clash.application.common.actor.Actor;
import java.time.Instant;
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
        List<RecordSessionData.Session> sessions
    ) {

        public static Result create(
            String date,
            Boolean pomodoroEnabled,
            Long totalStudyTime,
            Instant studyStoppedAt,
            List<RecordSessionData.Session> sessions
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
}
