package com.process.clash.adapter.web.roadmap.v2.chapter.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "챕터 수정 요청")
public class UpdateChapterV2RequestDoc {

    @Schema(description = "챕터 제목", example = "자바 기초")
    public String title;

    @Schema(description = "챕터 설명", example = "자바의 기본 문법을 학습합니다.")
    public String description;

    @Schema(description = "정렬 순서", example = "0")
    public Integer orderIndex;
}
