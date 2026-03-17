package com.process.clash.application.group.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.group.vo.GroupMemberVo;
import java.time.LocalDate;
import java.util.List;

public class GetGroupActivityData {

    public record Command(
        Actor actor,
        Long groupId,
        LocalDate date
    ) {
        public static Command of(Actor actor, Long groupId, LocalDate date) {
            return new Command(actor, groupId, date);
        }
    }

    public record Result(List<GroupMemberVo> members) {}
}
