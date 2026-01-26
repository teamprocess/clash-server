package com.process.clash.application.group.vo;

import com.process.clash.domain.group.entity.Group;
import com.process.clash.domain.group.enums.GroupCategory;
import com.process.clash.domain.user.user.entity.User;

public record GroupSummaryVo(
    Long id,
    String name,
    String description,
    Integer maxMembers,
    Integer currentMemberCount,
    GroupCategory category,
    Boolean passwordRequired,
    GroupOwnerVo owner
) {
    public static GroupSummaryVo from(Group group, User owner, Integer currentMemberCount) {
        return new GroupSummaryVo(
            group.id(),
            group.name(),
            group.description(),
            group.maxMembers(),
            currentMemberCount,
            group.category(),
            group.passwordRequired(),
            GroupOwnerVo.from(owner)
        );
    }
}
