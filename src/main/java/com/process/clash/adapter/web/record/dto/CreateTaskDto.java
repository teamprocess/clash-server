package com.process.clash.adapter.web.record.dto;

import com.process.clash.domain.record.model.enums.TaskColor;

public class CreateTaskDto {

    public record Request(
        String name,
        TaskColor color
    ) {}
}
