package com.process.clash.adapter.web.record.v2.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.record.v2.dto.StopRecordV2Dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "V2 기록 종료 응답")
public class StopRecordV2ResponseDocument extends SuccessResponseDocument {

    @Schema(description = "기록 종료 결과", implementation = StopRecordV2Dto.Response.class)
    public StopRecordV2Dto.Response data;
}
