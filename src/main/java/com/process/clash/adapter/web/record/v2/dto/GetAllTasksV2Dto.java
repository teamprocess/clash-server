package com.process.clash.adapter.web.record.v2.dto;

import com.process.clash.application.record.v2.data.GetAllTasksV2Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class GetAllTasksV2Dto {

    private static final String DEFAULT_ICON = "timer";

    @Schema(name = "GetAllTasksV2DtoResponse")
    public record Response(
        List<TaskSummary> tasks
    ) {
        public static Response from(GetAllTasksV2Data.Result result) {
            return new Response(
                result.tasks().stream()
                    .map(TaskSummary::from)
                    .toList()
            );
        }
    }

    public record TaskSummary(
        Long id,
        Long subjectId,
        String name,
        String icon,
        boolean completed,
        Long studyTime
    ) {
        public static TaskSummary from(GetAllTasksV2Data.TaskSummary task) {
            return new TaskSummary(
                task.id(),
                task.subjectId(),
                task.name(),
                DEFAULT_ICON,
                task.completed(),
                task.studyTime()
            );
        }
    }
}
