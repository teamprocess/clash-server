package com.process.clash.application.group.policy;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.group.exception.exception.forbidden.GroupAccessDeniedException;
import com.process.clash.domain.group.entity.Group;
import org.springframework.stereotype.Component;

@Component
public class GroupPolicy {

    public void validateOwnership(Actor actor, Group group) {
        if (!group.owner().id().equals(actor.id())) {
            throw new GroupAccessDeniedException();
        }
    }

    public void validateMembership(boolean isMember) {
        if (!isMember) {
            throw new GroupAccessDeniedException();
        }
    }
}
