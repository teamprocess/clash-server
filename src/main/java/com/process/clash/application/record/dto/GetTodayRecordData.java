package com.process.clash.application.record.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.record.model.entity.Task;
import java.util.Date;
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
        Date studyStoppedAt,
        List<Task> tasks
    ) {}
}
