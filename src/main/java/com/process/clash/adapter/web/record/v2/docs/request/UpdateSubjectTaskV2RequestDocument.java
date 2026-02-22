package com.process.clash.adapter.web.record.v2.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "V2 세부 작업 수정 요청")
public class UpdateSubjectTaskV2RequestDocument {

    @Schema(description = "세부 작업 이름", example = "ERD 검토")
    public String name;
}
