package com.process.clash.adapter.web.major.dto;

import com.process.clash.application.major.data.GetMajorQuestionData;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class GetMajorQuestionDto {

    public record Response(List<MajorQuestionVo> majorQuestions) {
        public static Response from(GetMajorQuestionData.Result result) {
            // Result 내부의 List<MajorQuestionVo>를 Web용 List<MajorQuestionVo>로 변환
            List<MajorQuestionVo> vos = result.getMajorQuestionVos().stream()
                    .map(MajorQuestionVo::from)
                    .toList();

            return new Response(vos);
        }
    }

    public record MajorQuestionVo(Long id, String content, MajorWeightVo weight) {
        // Result 내부의 개별 Vo를 인자로 받아 변환
        public static MajorQuestionVo from(GetMajorQuestionData.Result.MajorQuestionVo vo) {
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
        public static MajorWeightVo from(GetMajorQuestionData.Result.MajorQuestionVo.WeightVo vo) {
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