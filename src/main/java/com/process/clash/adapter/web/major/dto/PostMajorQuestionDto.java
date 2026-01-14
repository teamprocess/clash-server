package com.process.clash.adapter.web.major.dto;

import com.process.clash.application.major.data.GetMajorQuestionData;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class PostMajorQuestionDto {

    public record Request(
            String content,
            MajorWeightVo weight
    ) {}

    public record Response(
            Long questionId,
            String content,
            MajorWeightVo weight,
            LocalDateTime createdAt
    ) {}

    @Getter
    @AllArgsConstructor
    public static class MajorWeightVo {

        private final Integer web;
        private final Integer app;
        private final Integer server;
        private final Integer ai;
        private final Integer game;

        // Result 내부의 WeightVo를 인자로 받아 변환
        public static GetMajorQuestionDto.MajorWeightVo from(GetMajorQuestionData.Result.MajorQuestionVo.WeightVo vo) {

            return new GetMajorQuestionDto.MajorWeightVo(
                    vo.getWeb(),
                    vo.getApp(),
                    vo.getServer(),
                    vo.getAi(),
                    vo.getGame()
            );
        }
    }
}
