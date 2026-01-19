package com.process.clash.adapter.web.major.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "전공 질문 수정 요청")
public class UpdateMajorQuestionRequestDoc {

    @Schema(description = "질문 내용")
    public String content;

    @Schema(description = "전공 가중치")
    public MajorWeightDoc weight;

    public static class MajorWeightDoc {
        @Schema(description = "웹 가중치")
        public Integer web;
        @Schema(description = "앱 가중치")
        public Integer app;
        @Schema(description = "서버 가중치")
        public Integer server;
        @Schema(description = "AI 가중치")
        public Integer ai;
        @Schema(description = "게임 가중치")
        public Integer game;
    }
}
