package com.process.clash.application.roadmap.v2.question.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.roadmap.v2.entity.QuestionV2;

public class UpdateQuestionV2Data {

    public record Command(
            Actor actor,
            Long questionId,
            String content,
            String explanation,
            Integer orderIndex,
            Integer difficulty
    ) {
    }

    public record Result(
            Long questionId,
            String content,
            String explanation,
            Integer orderIndex,
            Integer difficulty
    ) {
        public static Result from(QuestionV2 question) {
            return new Result(
                    question.getId(),
                    question.getContent(),
                    question.getExplanation(),
                    question.getOrderIndex(),
                    question.getDifficulty()
            );
        }
    }
}
