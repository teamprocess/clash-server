package com.process.clash.application.roadmap.missions.data;

import com.process.clash.application.common.actor.Actor;

public class SubmitMissionAnswerData {

    public record Command(
            Actor actor,
            Long missionId,
            Long questionId,
            Long submittedChoiceId
    ) {
    }

    public record Result(
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
    }
}