package com.process.clash.adapter.web.group.dto;

import com.process.clash.application.group.data.GetGroupDetailData;

public class GetGroupDetailDto {

    public record Response(
        GroupSummaryDto group
    ) {
        public static Response from(GetGroupDetailData.Result result) {
            return new Response(GroupSummaryDto.from(result.group(), result.isMember()));
        }
    }
}
