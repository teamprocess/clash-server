package com.process.clash.application.roadmap.v2.question.data;

import com.process.clash.application.common.actor.Actor;

public class DeleteQuestionV2Data {

    public record Command(
            Actor actor,
            Long questionId
    ) {
    }
}
