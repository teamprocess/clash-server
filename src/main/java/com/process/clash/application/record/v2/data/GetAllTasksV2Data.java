package com.process.clash.application.record.v2.data;

import com.process.clash.application.common.actor.Actor;
import java.util.List;

public class GetAllTasksV2Data {

    public record Command(
        Actor actor
    ) {
    }

    public record Result(
        List<TaskSummary> tasks
    ) {
        public static Result from(List<TaskSummary> tasks) {
            return new Result(tasks);
        }
    }

    public record TaskSummary(
        Long id,
        Long subjectId,
        String name,
        boolean completed,
        Long studyTime
    ) {
    }
}
