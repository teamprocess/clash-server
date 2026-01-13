package com.process.clash.infrastructure.principle;

import com.process.clash.application.common.actor.Actor;

public record AuthUser(
        Long userId
) {
    public Actor toActor() {
        return new Actor(userId);
    }
}

