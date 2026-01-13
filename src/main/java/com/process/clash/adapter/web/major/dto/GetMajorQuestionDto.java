package com.process.clash.adapter.web.major.dto;

import com.process.clash.application.major.data.GetMajorQuestionResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class GetMajorQuestionDto {

    @Getter
    @AllArgsConstructor
    public static class Response {
        private final List<MajorQuestionVo> majorQuestions;

        public static Response from(GetMajorQuestionResult result) {
            // Result 내부의 List<MajorQuestionVo>를 Web용 List<MajorQuestionVo>로 변환
            List<MajorQuestionVo> vos = result.getMajorQuestionVos().stream()
                    .map(MajorQuestionVo::from)
                    .toList();

            return new Response(vos);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class MajorQuestionVo {
        private final Long id;
        private final String content; // Result의 content를 사용
        private final MajorWeightVo weight;

        // Result 내부의 개별 Vo를 인자로 받아 변환
        public static MajorQuestionVo from(GetMajorQuestionResult.MajorQuestionVo vo) {
            return new MajorQuestionVo(
                    vo.getId(),
                    vo.getContent(),
                    MajorWeightVo.from(vo.getWeightVo())
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class MajorWeightVo {
        private final Integer web;
        private final Integer app;
        private final Integer server;
        private final Integer ai;
        private final Integer game;

        // Result 내부의 WeightVo를 인자로 받아 변환
        public static MajorWeightVo from(GetMajorQuestionResult.MajorQuestionVo.WeightVo vo) {
            return new MajorWeightVo(
                    vo.getWeb(),
                    vo.getApp(),
                    vo.getServer(),
                    vo.getAi(),
                    vo.getGame()
            );
        }
    }
}