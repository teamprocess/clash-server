package com.process.clash.adapter.web.roadmap.v2.question.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "문제 수정 요청")
public class UpdateQuestionV2RequestDocument {

    @Schema(description = "문제 내용", example = "자바의 주요 특징은?")
    public String content;

    @Schema(description = "해설", example = "자바는 객체지향, 플랫폼 독립적, 멀티스레드를 지원합니다.")
    public String explanation;

    @Schema(description = "정렬 순서", example = "0")
    public Integer orderIndex;

    @Schema(description = "난이도 (1~5)", example = "2")
    public Integer difficulty;
}
