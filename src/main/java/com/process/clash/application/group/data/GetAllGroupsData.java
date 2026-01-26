package com.process.clash.application.group.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.common.pagination.Pagination;
import com.process.clash.application.group.vo.GroupSummaryVo;
import java.util.List;
import java.util.Set;

public class GetAllGroupsData {

    public record Command(
        Actor actor,
        Integer page
    ) {
        public Command {
            if (page == null || page < 1) {
                page = 1;
            }
        }
    }

    public record Result(
        List<GroupSummaryVo> groups,
        Pagination pagination,
        Set<Long> memberGroupIds
    ) {}
}
