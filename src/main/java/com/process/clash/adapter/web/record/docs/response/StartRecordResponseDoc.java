package com.process.clash.adapter.web.record.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.record.dto.StartRecordDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "일반 기록 시작 응답")
public class StartRecordResponseDoc extends SuccessResponseDoc {

    @Schema(description = "기록 시작 정보", implementation = StartRecordDto.Response.class)
    public StartRecordDto.Response data;
}
