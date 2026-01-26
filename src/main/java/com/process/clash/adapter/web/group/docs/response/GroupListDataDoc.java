package com.process.clash.adapter.web.group.docs.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "그룹 목록 데이터")
public class GroupListDataDoc {

    @Schema(description = "그룹 목록")
    public List<GroupSummaryDoc> groups;

    @Schema(description = "페이지네이션")
    public GroupPaginationDoc pagination;
}
