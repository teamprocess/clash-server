package com.process.clash.application.group.data;

import com.process.clash.application.common.actor.Actor;

public class DeleteGroupData {

    public record Command(
        Actor actor,
        Long groupId
    ) {}
}
