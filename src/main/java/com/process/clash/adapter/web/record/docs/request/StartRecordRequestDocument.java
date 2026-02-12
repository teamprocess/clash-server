package com.process.clash.adapter.web.record.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "일반 기록 시작 요청")
public class StartRecordRequestDocument {

    @Schema(description = "기록 유형", example = "TASK")
    public String recordType;

    @Schema(description = "과목 ID (recordType=TASK 일 때 필수)")
    public Long taskId;

    @Schema(description = "앱 이름 (recordType=ACTIVITY 일 때 필수)", example = "Code")
    public String appName;
}
