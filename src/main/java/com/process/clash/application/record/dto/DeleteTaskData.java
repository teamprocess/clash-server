package com.process.clash.application.record.dto;

import com.process.clash.application.common.actor.Actor;

public class DeleteTaskData {

    public record Command(
        Actor actor,
        Long taskId
    ) {}
}
