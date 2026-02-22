package com.process.clash.adapter.web.record.v2.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "V2 과목 그룹 생성 요청")
public class CreateSubjectV2RequestDocument {

    @Schema(description = "과목 그룹 이름", example = "백엔드 프로젝트")
    public String name;
}
