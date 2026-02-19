package com.process.clash.adapter.web.record.dto;

import com.process.clash.application.record.data.UpdateTaskData;
import com.process.clash.domain.record.entity.RecordTask;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class UpdateTaskDto {

    @Schema(name = "UpdateTaskDtoRequest")

    public record Request(
        @NotBlank(message = "name은 필수 입력값입니다.")
        String name
    ) {}

    @Schema(name = "UpdateTaskDtoResponse")

    public record Response(
        Long id,
        String name,
        Long studyTime
    ) {
        public static Response from(UpdateTaskData.Result result) {
            RecordTask task = result.task();
            return new Response(
                task.id(),
                task.name(),
                task.studyTime()
            );
        }
    }
}
