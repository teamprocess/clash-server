package com.process.clash.infrastructure.principle;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.Role;

public record AuthUser(
        Long id,
        Role role
) {
    public Actor toActor() {
        return new Actor(id, role);
    }
}

