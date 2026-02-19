package com.process.clash.application.record.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.record.entity.RecordTask;
import java.util.List;

public class GetAllTasksData {

    public record Command (
        Actor actor
    ) {}

    public record Result (
        List<RecordTask> tasks
    ) {
        public static Result create(List<RecordTask> tasks) {
            return new Result(tasks);
        }
    }
}
