package com.process.clash.adapter.web.group.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.common.pagination.Pagination;
import com.process.clash.application.group.data.GetMyGroupsData;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class GetMyGroupsDto {

    @Schema(name = "GetMyGroupsRequest")
    public record Request(
        @Schema(description = "페이지 번호 (1부터)", example = "1")
        Integer page
    ) {
        public GetMyGroupsData.Command toCommand(Actor actor) {
            return new GetMyGroupsData.Command(actor, page);
        }
    }

    @Schema(name = "GetMyGroupsResponse")
    public record Response(
        @Schema(description = "그룹 목록")
        List<GroupSummaryDto> groups,
        @Schema(description = "페이지네이션")
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
