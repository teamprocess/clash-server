package com.process.clash.adapter.web.group.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.common.pagination.Pagination;
import com.process.clash.application.group.data.GetAllGroupsData;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class GetAllGroupsDto {

    @Schema(name = "GetAllGroupsRequest")
    public record Request(
        @Schema(description = "페이지 번호 (1부터)", example = "1")
        Integer page
    ) {
        public GetAllGroupsData.Command toCommand(Actor actor) {
            return new GetAllGroupsData.Command(actor, page);
        }
    }

    @Schema(name = "GetAllGroupsResponse")
    public record Response(
        @Schema(description = "그룹 목록")
        List<GroupSummaryDto> groups,
        @Schema(description = "페이지네이션")
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
