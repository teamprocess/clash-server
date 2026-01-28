package com.process.clash.adapter.web.group.dto;

import com.process.clash.adapter.web.group.util.GroupCategoryConverter;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.common.pagination.Pagination;
import com.process.clash.application.group.data.GetMyGroupsData;
import java.util.List;

public class GetMyGroupsDto {

    public record Request(
        Integer page,
        String category
    ) {
        public GetMyGroupsData.Command toCommand(Actor actor) {
            return new GetMyGroupsData.Command(actor, page, GroupCategoryConverter.toCategoryFilter(category));
        }
    }

    public record Response(
        List<GroupSummaryDto> groups,
        Pagination pagination
    ) {
        public static Response from(GetMyGroupsData.Result result) {
            List<GroupSummaryDto> groups = result.groups().stream()
                .map(group -> GroupSummaryDto.from(group, true))
                .toList();
            return new Response(groups, result.pagination());
        }
    }
}
