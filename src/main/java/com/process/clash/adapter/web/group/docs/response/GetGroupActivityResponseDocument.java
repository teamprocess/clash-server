package com.process.clash.adapter.web.group.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "그룹 활동 조회 응답")
public class GetGroupActivityResponseDocument extends SuccessResponseDocument {

    @Schema(description = "그룹 활동 정보")
    public GroupActivityDataDocument data;
}
