package com.process.clash.adapter.web.record.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.record.dto.StopRecordDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "일반 기록 종료 응답")
public class StopRecordResponseDoc extends SuccessResponseDoc {

    @Schema(description = "기록 종료 정보", implementation = StopRecordDto.Response.class)
    public StopRecordDto.Response data;
}
