package com.process.clash.adapter.web.roadmap.missions.dto;

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
            Long correctChoiceId,
            boolean isMissionCleared,
            Long nextMissionId,
            Integer nextMissionOrderIndex,
            boolean isChapterCleared,
            Long nextChapterId,
            Integer nextChapterOrderIndex
    ) {
        public static Response fromResult(SubmitMissionAnswerData.Result result) {
            return new Response(
                    result.isCorrect(),
                    result.explanation(),
                    result.currentProgress(),
                    result.totalQuestion(),
                    result.correctChoiceId(),
                    result.isMissionCleared(),
                    result.nextMissionId(),
                    result.nextMissionOrderIndex(),
                    result.isChapterCleared(),
                    result.nextChapterId(),
                    result.nextChapterOrderIndex()
            );
        }
    }
}