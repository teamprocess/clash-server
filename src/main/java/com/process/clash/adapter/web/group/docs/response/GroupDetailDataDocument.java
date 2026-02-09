package com.process.clash.adapter.web.group.docs.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "그룹 상세 데이터")
public class GroupDetailDataDocument {

    @Schema(description = "그룹 정보")
    public GroupSummaryDocument group;
}
