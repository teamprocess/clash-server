package com.process.clash.domain.common.policy;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.user.user.exception.exception.forbidden.RequiredAdminRoleException;
import org.springframework.stereotype.Component;

@Component
public class CheckAdminPolicy {

    public void check(Actor actor) {
        if (!actor.isAdmin())
            throw new RequiredAdminRoleException();
    }
}
