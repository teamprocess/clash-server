package com.process.clash.application.group.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.common.pagination.Pagination;
import com.process.clash.application.group.vo.GroupMemberVo;
import java.time.LocalDate;
import java.util.List;

public class GetGroupActivityData {

    public record Command(
        Actor actor,
        Long groupId,
        Integer page,
        LocalDate date
    ) {
        public Command {
            if (page == null || page < 1) {
                page = 1;
            }
        }

        public static Command of(Actor actor, Long groupId, Integer page, LocalDate date) {
            return new Command(actor, groupId, page, date);
        }
    }

    public record Result(
        List<GroupMemberVo> members,
        Pagination pagination
    ) {}
}
