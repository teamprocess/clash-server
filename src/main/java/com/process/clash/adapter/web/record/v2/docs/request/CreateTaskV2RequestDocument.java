package com.process.clash.adapter.web.record.v2.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "V2 세부 작업 생성 요청")
public class CreateTaskV2RequestDocument {

    @Schema(description = "과목 그룹 ID (선택)", example = "12")
    public Long subjectId;

    @Schema(description = "세부 작업 이름", example = "ERD 설계")
    public String name;
}
