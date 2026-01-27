package com.process.clash.adapter.web.group.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.common.pagination.Pagination;
import com.process.clash.application.group.data.GetAllGroupsData;
import com.process.clash.domain.group.enums.GroupCategory;
import java.util.List;
import java.util.Locale;

public class GetAllGroupsDto {

    public record Request(
        Integer page,
        String category
    ) {
        public GetAllGroupsData.Command toCommand(Actor actor) {
            return new GetAllGroupsData.Command(actor, page, toCategoryFilter(category));
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

    private static GroupCategory toCategoryFilter(String category) {
        if (category == null || category.isBlank() || "ALL".equalsIgnoreCase(category)) {
            return null;
        }
        return GroupCategory.valueOf(category.toUpperCase(Locale.ROOT));
    }
}
