package com.process.clash.application.roadmap.v2.question.data;

import com.process.clash.application.common.actor.Actor;

public class SubmitQuestionV2AnswerData {

    public record Command(
            Actor actor,
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
            boolean isChapterCleared,
            Long nextChapterId,
            Integer nextChapterOrderIndex
    ) {
    }
}
