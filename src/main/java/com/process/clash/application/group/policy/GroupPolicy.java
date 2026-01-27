package com.process.clash.application.group.policy;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.common.exception.exception.ValidationException;
import com.process.clash.application.group.exception.exception.conflict.GroupMemberLimitReachedException;
import com.process.clash.application.group.exception.exception.conflict.GroupOwnerCannotQuitException;
import com.process.clash.application.group.exception.exception.forbidden.GroupAccessDeniedException;
import com.process.clash.domain.group.entity.Group;
import org.springframework.stereotype.Component;

@Component
public class GroupPolicy {

    public void validateOwnership(Actor actor, Group group) {
        if (!group.ownerId().equals(actor.id())) {
            throw new GroupAccessDeniedException();
        }
    }

    public void validateMembership(boolean isMember) {
        if (!isMember) {
            throw new GroupAccessDeniedException();
        }
    }

    public void validateMaxMembers(Integer maxMembers) {
        if (maxMembers == null || maxMembers < 1) {
            throw new ValidationException();
        }
    }

    public void validateMemberLimit(int maxMembers, int memberCount) {
        if (memberCount >= maxMembers) {
            throw new GroupMemberLimitReachedException();
        }
    }

    public void canQuitGroup(Actor actor, Group group) {
        if (group.ownerId().equals(actor.id())) {
            throw new GroupOwnerCannotQuitException();
        }
    }
}
