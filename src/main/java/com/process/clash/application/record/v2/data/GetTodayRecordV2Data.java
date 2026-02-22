package com.process.clash.application.record.v2.data;

import com.process.clash.application.common.actor.Actor;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

public class GetTodayRecordV2Data {

    @Builder
    public record Command(
        Actor actor,
        LocalDate date
    ) {
    }

    @Builder
    public record Result(
        String date,
        Long totalStudyTime,
        Instant studyStoppedAt,
        List<RecordSessionV2Data.Session> sessions
    ) {
        public static Result from(
            String date,
            Long totalStudyTime,
            Instant studyStoppedAt,
            List<RecordSessionV2Data.Session> sessions
        ) {
            return new Result(date, totalStudyTime, studyStoppedAt, sessions);
        }
    }
}
