package com.process.clash.adapter.web.record.v2.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.record.v2.dto.GetTodayRecordV2Dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "V2 일자별 기록 조회 응답")
public class GetDailyRecordV2ResponseDocument extends SuccessResponseDocument {

    @Schema(description = "일자별 기록", implementation = GetTodayRecordV2Dto.Response.class)
    public GetTodayRecordV2Dto.Response data;
}
