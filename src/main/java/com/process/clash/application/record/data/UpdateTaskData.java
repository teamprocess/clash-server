package com.process.clash.application.record.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.record.entity.Task;

public class UpdateTaskData {

    public record Command(
        Actor actor,
        Long taskId,
        String name
    ) {}

    public record Result(
        Task task
    ) {
        public static Result from(Task task) {
            return new Result(task);
        }
    }
}
