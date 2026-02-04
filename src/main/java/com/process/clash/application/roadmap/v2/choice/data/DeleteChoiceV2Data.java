package com.process.clash.application.roadmap.v2.choice.data;

import com.process.clash.application.common.actor.Actor;

public class DeleteChoiceV2Data {

    public record Command(
            Actor actor,
            Long choiceId
    ) {
    }
}
