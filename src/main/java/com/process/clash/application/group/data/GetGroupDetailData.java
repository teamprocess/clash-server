package com.process.clash.application.group.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.group.vo.GroupSummaryVo;

public class GetGroupDetailData {

    public record Command(
        Actor actor,
        Long groupId
    ) {}

    public record Result(
        GroupSummaryVo group,
        Boolean isMember
    ) {}
}
