package com.process.clash.application.record.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.record.model.entity.Task;
import java.util.Date;
import java.util.List;

public class GetTodayRecordData {

    public record Commmand(
        Actor actor
    ) {}

    public record Result(
        String date,
        Boolean pomodoro_enabled,
        Long totalStudyTime,
        Date studyStoppedAt,
        List<Task> tasks
    ) {}
}
