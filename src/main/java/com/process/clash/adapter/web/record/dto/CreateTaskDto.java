package com.process.clash.adapter.web.record.dto;

import com.process.clash.domain.record.model.enums.TaskColor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateTaskDto {

    @Schema(name = "CreateTaskDtoRequest")

    public record Request(
        @NotBlank(message = "name은 필수 입력값입니다.")
        String name,
        @NotNull(message = "color는 필수 입력값입니다.")
        TaskColor color
    ) {}
}
