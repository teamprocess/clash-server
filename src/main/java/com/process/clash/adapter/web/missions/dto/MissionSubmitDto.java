package com.process.clash.adapter.web.missions.dto;

import com.process.clash.application.roadmap.missions.data.SubmitMissionAnswerData;

public class MissionSubmitDto {

    public record Request(
            Long submittedChoiceId
    ) {
    }

    public record Response(
            boolean isCorrect,
            String explanation,
            Integer currentProgress,
            Integer totalQuestion,
            Long correctChoiceId
    ) {
        public static Response fromResult(SubmitMissionAnswerData.Result result) {
            return new Response(
                    result.isCorrect(),
                    result.explanation(),
                    result.currentProgress(),
                    result.totalQuestion(),
                    result.correctChoiceId()
            );
        }
    }
}