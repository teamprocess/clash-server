package com.process.clash.application.record.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.record.model.entity.Task;
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
        Boolean pomodoro_enabled,
        Long totalStudyTime,
        Instant studyStoppedAt,
        List<Task> tasks
    ) {

        public static Result create(String date, Boolean pomodoro_enabled, Long totalStudyTime, Instant studyStoppedAt, List<Task> tasks) {
            return new Result(
                date,
                pomodoro_enabled,
                totalStudyTime,
                studyStoppedAt,
                tasks
            );
        }
    }
}
