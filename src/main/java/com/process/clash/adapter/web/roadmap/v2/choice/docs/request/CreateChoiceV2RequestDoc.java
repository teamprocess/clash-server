package com.process.clash.adapter.web.roadmap.v2.choice.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "선택지 생성 요청")
public class CreateChoiceV2RequestDoc {

    @Schema(description = "문제 ID", example = "1")
    public Long questionId;

    @Schema(description = "선택지 내용", example = "객체지향")
    public String content;

    @Schema(description = "정답 여부", example = "true")
    public Boolean isCorrect;
}
