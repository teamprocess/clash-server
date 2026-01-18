package com.process.clash.adapter.web.record.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "일반 기록 시작 요청")
public class StartRecordRequestDoc {

    @Schema(description = "과목 ID")
    public Long taskId;
}
