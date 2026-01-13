package com.process.clash.adapter.web.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class GetMajorQuestionDto {

    @Getter
    @AllArgsConstructor
    public static class Response {
        private List<MajorQuestionVo> majorQuestions;
    }

    @Getter
    @AllArgsConstructor
    public static class MajorQuestionVo {
        private Long id;
        private String title;
        private String subTitle;
        private MajorWeightVo weight;
    }

    @Getter
    @AllArgsConstructor
    public static class MajorWeightVo {
        private Integer web;
        private Integer app;
        private Integer server;
        private Integer ai;   // API 명세의 "AI" 대응
        private Integer game;
    }
}