package com.process.clash.adapter.web.record.dto;

import com.process.clash.application.record.dto.GetAllTasksData;
import com.process.clash.domain.record.model.entity.Task;
import com.process.clash.domain.record.model.enums.TaskColor;
import java.util.List;

public class GetAllTasksDto {

    public record Response(
        List<TaskSummary> tasks
    ) {
        public static Response from(GetAllTasksData.Result result){
            return new Response(
                result.tasks().stream()
                    .map(TaskSummary::from)
                    .toList()
            );
        }
    }

    public record TaskSummary(
        Long id,
        String name,
        String icon,
        TaskColor color,
        Long studyTime
    ) {
        public static TaskSummary from(Task task) {
            return new TaskSummary(
                task.id(),
                task.name(),
                "timer",
                task.color(),
                task.studyTime()
            );
        }
    }
}
