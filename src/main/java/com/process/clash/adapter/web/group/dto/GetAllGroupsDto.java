package com.process.clash.adapter.web.group.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.common.pagination.Pagination;
import com.process.clash.application.group.data.GetAllGroupsData;
import java.util.List;

public class GetAllGroupsDto {

    public record Request(
        Integer page
    ) {
        public GetAllGroupsData.Command toCommand(Actor actor) {
            return new GetAllGroupsData.Command(actor, page);
        }
    }

    public record Response(
        List<GroupSummaryDto> groups,
        Pagination pagination
    ) {
        public static Response from(GetAllGroupsData.Result result) {
            List<GroupSummaryDto> groups = result.groups().stream()
                .map(group -> GroupSummaryDto.from(
                    group,
                    result.memberGroupIds().contains(group.id())
                ))
                .toList();
            return new Response(groups, result.pagination());
        }
    }
}
