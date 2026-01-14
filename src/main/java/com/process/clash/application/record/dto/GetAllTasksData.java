package com.process.clash.application.record.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.record.model.entity.Task;
import java.util.List;

public class GetAllTasksData {

    public record Command (
        Actor actor
    ) {}

    public record Result (
        List<Task> tasks
    ) {
        public static Result create(List<Task> tasks) {
            return new Result(tasks);
        }
    }
}
