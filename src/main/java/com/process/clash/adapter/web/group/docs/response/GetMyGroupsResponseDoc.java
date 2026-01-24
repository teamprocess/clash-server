package com.process.clash.adapter.web.group.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.group.dto.GetMyGroupsDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "내 그룹 목록 조회 응답")
public class GetMyGroupsResponseDoc extends SuccessResponseDoc {

    @Schema(description = "내 그룹 목록")
    public GetMyGroupsDto.Response data;
}
