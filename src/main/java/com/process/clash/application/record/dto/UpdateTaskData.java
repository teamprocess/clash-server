package com.process.clash.application.record.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.record.model.entity.Task;
import com.process.clash.domain.record.model.enums.TaskColor;

public class UpdateTaskData {

    public record Command(
        Actor actor,
        Long taskId,
        String name,
        TaskColor color
    ) {}

    public record Result(
        Task task
    ) {
        public static Result from(Task task) {
            return new Result(task);
        }
    }
}
