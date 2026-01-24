package com.process.clash.application.group.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.group.enums.GroupCategory;

public class UpdateGroupData {

    public record Command(
        Actor actor,
        Long groupId,
        String name,
        String description,
        Integer maxMembers,
        GroupCategory category,
        Boolean passwordRequired,
        String password
    ) {}
}
