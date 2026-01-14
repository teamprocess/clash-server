package com.process.clash.application.record.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.record.model.enums.TaskColor;

public class CreateTaskData {

    public record Command(
        Actor actor,
        String name,
        TaskColor color
    ) {}
}
