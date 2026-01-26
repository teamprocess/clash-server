package com.process.clash.application.group.data;

import com.process.clash.application.common.actor.Actor;

public class JoinGroupData {

    public record Command(
        Actor actor,
        Long groupId,
        String password
    ) {}
}
