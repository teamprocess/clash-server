package com.process.clash.adapter.web.group.docs.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "그룹 활동 데이터")
public class GroupActivityDataDoc {

    @Schema(description = "멤버 목록")
    public List<GroupActivityMemberDoc> members;

    @Schema(description = "페이지네이션")
    public GroupPaginationDoc pagination;
}
