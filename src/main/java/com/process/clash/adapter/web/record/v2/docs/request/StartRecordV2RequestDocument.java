package com.process.clash.adapter.web.record.v2.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "V2 기록 시작 요청")
public class StartRecordV2RequestDocument {

    @Schema(description = "세션 유형", example = "TASK", requiredMode = Schema.RequiredMode.REQUIRED)
    public String sessionType;

    @Schema(description = "과목 그룹 ID (sessionType=TASK 일 때 필수)", example = "1")
    public Long subjectId;

    @Schema(description = "세부 작업 ID (선택)", example = "10")
    public Long taskId;

    @Schema(description = "개발 앱 ID (sessionType=DEVELOP 일 때 필수)", example = "VSCODE")
    public String appId;
}
