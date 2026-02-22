package com.process.clash.adapter.web.record.v2.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.record.v2.dto.StartRecordV2Dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "V2 기록 시작 응답")
public class StartRecordV2ResponseDocument extends SuccessResponseDocument {

    @Schema(description = "기록 시작 결과", implementation = StartRecordV2Dto.Response.class)
    public StartRecordV2Dto.Response data;
}
