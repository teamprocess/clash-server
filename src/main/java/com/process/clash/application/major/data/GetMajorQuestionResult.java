package com.process.clash.application.major.data;

import com.process.clash.domain.major.MajorQuestion;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 외부에서 new 금지, from만 사용
public class GetMajorQuestionResult {

    private final List<MajorQuestionVo> majorQuestionVos;

    // 도메인 엔티티 리스트를 받아 Result(DTO)로 변환
    public static GetMajorQuestionResult from(List<MajorQuestion> domainModels) {
        return new GetMajorQuestionResult(
                domainModels.stream()
                        .map(MajorQuestionVo::from)
                        .toList()
        );
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MajorQuestionVo {
        private final Long id;
        private final String content;
        private final WeightVo weightVo;

        // 중요: 도메인 엔티티(MajorQuestion)를 인자로 받아야 함
        public static MajorQuestionVo from(MajorQuestion domain) {
            return new MajorQuestionVo(
                    domain.getId(),
                    domain.getContent(),
                    WeightVo.from(domain.getWeightVo()) // 도메인 내부의 WeightVo 변환
            );
        }

        @Getter
        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        public static class WeightVo {
            private final Integer web;
            private final Integer app;
            private final Integer server;
            private final Integer ai;
            private final Integer game;

            // 중요: 도메인 계층의 WeightVo를 인자로 받아 Application 계층의 WeightVo로 변환
            public static WeightVo from(MajorQuestion.WeightVo domainWeight) {
                return new WeightVo(
                        domainWeight.getWeb(),
                        domainWeight.getApp(),
                        domainWeight.getServer(),
                        domainWeight.getAi(),
                        domainWeight.getGame()
                );
            }
        }
    }
}