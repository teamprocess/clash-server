package com.process.clash.adapter.web.record.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.record.dto.GetTodayRecordDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "오늘의 기록 조회 응답")
public class GetTodayRecordResponseDocument extends SuccessResponseDocument {

    @Schema(description = "오늘의 기록", implementation = GetTodayRecordDto.Response.class)
    public GetTodayRecordDto.Response data;
}
