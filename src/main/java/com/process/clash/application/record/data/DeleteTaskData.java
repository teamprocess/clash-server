package com.process.clash.application.record.data;

import com.process.clash.application.common.actor.Actor;

public class DeleteTaskData {

    public record Command(
        Actor actor,
        Long taskId
    ) {}
}
