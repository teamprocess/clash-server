package com.process.clash.application.roadmap.v2.choice.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.roadmap.v2.entity.ChoiceV2;

public class CreateChoiceV2Data {

    public record Command(
            Actor actor,
            Long questionId,
            String content,
            boolean isCorrect
    ) {
        public ChoiceV2 toDomain() {
            return new ChoiceV2(
                    null,
                    questionId,
                    content,
                    isCorrect
            );
        }
    }

    public record Result(
            Long choiceId,
            Long questionId,
            String content,
            boolean isCorrect
    ) {
        public static Result from(ChoiceV2 choice) {
            return new Result(
                    choice.getId(),
                    choice.getQuestionId(),
                    choice.getContent(),
                    choice.isCorrect()
            );
        }
    }
}
