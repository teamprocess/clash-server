package com.process.clash.domain.common.policy;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.user.user.exception.exception.forbidden.RequiredUserRoleException;
import org.springframework.stereotype.Component;

@Component
public class CheckUserPolicy {

    public void check(Actor actor) {
        if (!actor.isUser())
            throw new RequiredUserRoleException();
    }
}
