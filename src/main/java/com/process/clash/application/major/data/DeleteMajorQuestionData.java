package com.process.clash.application.major.data;

import com.process.clash.application.common.actor.Actor;

public class DeleteMajorQuestionData {

    public record Command(Actor actor, Long questionId) {}
}
