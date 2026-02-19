package com.process.clash.domain.major.entity;

import lombok.*;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MajorQuestion {

    private Long id;

    private String content; // 질문 텍스트 (예: "논리적인 문제 해결을 즐긴다")

    private MajorQuestion.WeightVo weightVo;

    private Instant createdAt;

    private Instant updatedAt;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class WeightVo {

        private Integer web;

        private Integer app;

        private Integer server;

        private Integer ai;

        private Integer game;
    }
}
