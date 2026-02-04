package com.process.clash.adapter.web.roadmap.v2.choice.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "선택지 수정 요청")
public class UpdateChoiceV2RequestDoc {

    @Schema(description = "선택지 내용", example = "객체지향 프로그래밍")
    public String content;

    @Schema(description = "정답 여부", example = "true")
    public Boolean isCorrect;
}
