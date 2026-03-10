package com.process.clash.adapter.web.record.v2.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "V2 세부 작업 수정 요청")
public class UpdateTaskV2RequestDocument {

    @Schema(description = "부모 과목 ID, 없으면 null", example = "12", nullable = true)
    public Long subjectId;

    @Schema(description = "세부 작업 이름", example = "ERD 검토")
    public String name;
}
