package com.process.clash.application.roadmap.v2.choice.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.roadmap.v2.entity.ChoiceV2;

public class UpdateChoiceV2Data {

    public record Command(
            Actor actor,
            Long choiceId,
            String content,
            boolean isCorrect
    ) {
    }

    public record Result(
            Long choiceId,
            String content,
            boolean isCorrect
    ) {
        public static Result from(ChoiceV2 choice) {
            return new Result(
                    choice.getId(),
                    choice.getContent(),
                    choice.isCorrect()
            );
        }
    }
}
