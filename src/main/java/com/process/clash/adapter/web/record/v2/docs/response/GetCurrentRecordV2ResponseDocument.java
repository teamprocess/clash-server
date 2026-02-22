package com.process.clash.adapter.web.record.v2.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.record.v2.dto.RecordSessionV2Dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "V2 현재 기록 세션 조회 응답")
public class GetCurrentRecordV2ResponseDocument extends SuccessResponseDocument {

    @Schema(description = "현재 기록 세션", implementation = RecordSessionV2Dto.Session.class, nullable = true)
    public RecordSessionV2Dto.Session data;
}
