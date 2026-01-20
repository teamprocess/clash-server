package com.process.clash.adapter.web.record.dto;

import com.process.clash.application.record.data.UpdateTaskData;
import com.process.clash.domain.record.model.entity.Task;
import com.process.clash.domain.record.model.enums.TaskColor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateTaskDto {

    @Schema(name = "UpdateTaskDtoRequest")

    public record Request(
        @NotBlank(message = "name은 필수 입력값입니다.")
        String name,
        @NotNull(message = "color는 필수 입력값입니다.")
        TaskColor color
    ) {}

    @Schema(name = "UpdateTaskDtoResponse")

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
