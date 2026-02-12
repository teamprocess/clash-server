package com.process.clash.adapter.web.record.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.record.dto.RecordSessionDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "현재 기록 세션 조회 응답")
public class GetCurrentRecordResponseDocument extends SuccessResponseDocument {

    @Schema(description = "현재 기록 세션", implementation = RecordSessionDto.Session.class, nullable = true)
    public RecordSessionDto.Session data;
}
