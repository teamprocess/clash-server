package com.process.clash.application.record.data;

import com.process.clash.application.common.actor.Actor;
public class CreateTaskData {

    public record Command(
        Actor actor,
        String name
    ) {}
}
