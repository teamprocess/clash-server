package com.process.clash.adapter.web.group.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.common.pagination.Pagination;
import com.process.clash.application.group.data.GetMyGroupsData;
import com.process.clash.domain.group.enums.GroupCategory;
import java.util.List;
import java.util.Locale;

public class GetMyGroupsDto {

    public record Request(
        Integer page,
        String category
    ) {
        public GetMyGroupsData.Command toCommand(Actor actor) {
            return new GetMyGroupsData.Command(actor, page, toCategoryFilter(category));
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

    private static GroupCategory toCategoryFilter(String category) {
        if (category == null || category.isBlank() || "ALL".equalsIgnoreCase(category)) {
            return null;
        }
        return GroupCategory.valueOf(category.toUpperCase(Locale.ROOT));
    }
}
