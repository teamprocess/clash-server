package com.process.clash.adapter.web.group.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "그룹 상세 조회 응답")
public class GetGroupDetailResponseDoc extends SuccessResponseDoc {

    @Schema(description = "그룹 상세 정보")
    public GroupDetailDataDoc data;
}
