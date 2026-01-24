package com.process.clash.adapter.web.group.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.group.dto.GetGroupActivityDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "그룹 활동 조회 응답")
public class GetGroupActivityResponseDoc extends SuccessResponseDoc {

    @Schema(description = "그룹 활동 정보")
    public GetGroupActivityDto.Response data;
}
