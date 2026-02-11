package com.process.clash.adapter.web.record.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.record.dto.GetAllTasksDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "과목 목록 조회 응답")
public class GetAllTasksResponseDocument extends SuccessResponseDocument {

    @Schema(description = "과목 목록", implementation = GetAllTasksDto.Response.class)
    public GetAllTasksDto.Response data;
}
