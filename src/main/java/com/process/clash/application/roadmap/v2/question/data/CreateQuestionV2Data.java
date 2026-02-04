package com.process.clash.application.roadmap.v2.question.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.roadmap.v2.entity.QuestionV2;

import java.util.List;

public class CreateQuestionV2Data {

    public record Command(
            Actor actor,
            Long chapterId,
            String content,
            String explanation,
            Integer orderIndex,
            Integer difficulty
    ) {
        public QuestionV2 toDomain() {
            return new QuestionV2(
                    null,
                    chapterId,
                    content,
                    explanation,
                    orderIndex,
                    difficulty,
                    List.of()
            );
        }
    }

    public record Result(
            Long questionId,
            Long chapterId,
            String content,
            String explanation,
            Integer orderIndex,
            Integer difficulty
    ) {
        public static Result from(QuestionV2 question) {
            return new Result(
                    question.getId(),
                    question.getChapterId(),
                    question.getContent(),
                    question.getExplanation(),
                    question.getOrderIndex(),
                    question.getDifficulty()
            );
        }
    }
}
