package com.process.clash.adapter.web.roadmap.v2.question.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "문제 생성 요청")
public class CreateQuestionV2RequestDocument {

    @Schema(description = "챕터 ID", example = "1")
    public Long chapterId;

    @Schema(description = "문제 내용", example = "자바의 특징이 아닌 것은?")
    public String content;

    @Schema(description = "해설", example = "자바는 객체지향 언어입니다.")
    public String explanation;

    @Schema(description = "정렬 순서", example = "0")
    public Integer orderIndex;

    @Schema(description = "난이도 (1~5)", example = "1")
    public Integer difficulty;
}
