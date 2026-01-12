package com.process.clash.domain.major;

import com.process.clash.domain.common.enums.Major;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MajorQuestion {

    private Long id;

    private String content; // 질문 텍스트 (예: "논리적인 문제 해결을 즐긴다")

    private Major major;

    @Embedded
    private MajorQuestion.WeightVo weightVo;

    @Embeddable
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
