package com.process.clash.adapter.web.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class CreateTaskDto {

    @Schema(name = "CreateTaskDtoRequest")

    public record Request(
        @NotBlank(message = "name은 필수 입력값입니다.")
        String name
    ) {}
}
