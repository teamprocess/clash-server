package com.process.clash.adapter.web.group.dto;

import com.process.clash.application.group.vo.GroupOwnerVo;
import com.process.clash.application.group.vo.GroupSummaryVo;
import com.process.clash.domain.group.enums.GroupCategory;
public record GroupSummaryDto(
    Long id,
    String name,
    String description,
    Integer maxMembers,
    Integer currentMemberCount,
    GroupCategory category,
    Boolean passwordRequired,
    Owner owner,
    Boolean isMember
) {
    public static GroupSummaryDto from(GroupSummaryVo vo) {
        return from(vo, false);
    }

    public static GroupSummaryDto from(GroupSummaryVo vo, Boolean isMember) {
        return new GroupSummaryDto(
            vo.id(),
            vo.name(),
            vo.description(),
            vo.maxMembers(),
            vo.currentMemberCount(),
            vo.category(),
            vo.passwordRequired(),
            Owner.from(vo.owner()),
            isMember
        );
    }

    public record Owner(
        Long id,
        String name
    ) {
        public static Owner from(GroupOwnerVo owner) {
            return new Owner(owner.id(), owner.name());
        }
    }
}
