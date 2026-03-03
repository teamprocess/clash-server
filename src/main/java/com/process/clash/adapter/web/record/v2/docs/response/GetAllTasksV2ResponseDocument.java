package com.process.clash.adapter.web.record.v2.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.record.v2.dto.GetAllTasksV2Dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "V2 세부 작업 목록 조회 응답")
public class GetAllTasksV2ResponseDocument extends SuccessResponseDocument {

    @Schema(description = "세부 작업 목록", implementation = GetAllTasksV2Dto.Response.class)
    public GetAllTasksV2Dto.Response data;
}
