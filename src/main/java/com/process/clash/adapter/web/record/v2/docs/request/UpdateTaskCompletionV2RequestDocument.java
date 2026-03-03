package com.process.clash.adapter.web.record.v2.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "V2 세부 작업 완료 상태 변경 요청")
public class UpdateTaskCompletionV2RequestDocument {

    @Schema(description = "완료 여부", example = "true")
    public Boolean completed;
}
