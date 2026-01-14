package com.process.clash.adapter.web.record.dto;

import com.process.clash.application.record.dto.UpdateTaskData;
import com.process.clash.domain.record.model.entity.Task;
import com.process.clash.domain.record.model.enums.TaskColor;

public class UpdateTaskDto {

    public record Request(
        String name,
        TaskColor color
    ) {}

    public record Response(
        Long id,
        String name,
        TaskColor color,
        Long studyTime
    ) {
        public static Response from(UpdateTaskData.Result result) {
            Task task = result.task();
            return new Response(
                task.id(),
                task.name(),
                task.color(),
                task.studyTime()
            );
        }
    }
}
