package com.process.clash.adapter.web.record.dto;

import com.process.clash.domain.record.model.enums.TaskColor;
import io.swagger.v3.oas.annotations.media.Schema;

public class CreateTaskDto {

    @Schema(name = "CreateTaskDtoRequest")

    public record Request(
        String name,
        TaskColor color
    ) {}
}
