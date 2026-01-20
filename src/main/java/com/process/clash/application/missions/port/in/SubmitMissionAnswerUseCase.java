package com.process.clash.application.missions.port.in;

import com.process.clash.application.common.actor.Actor;

public interface SubmitMissionAnswerUseCase {

    Result execute(Command command);

    record Command(
            Actor actor,
            Long missionId,
            Long questionId,
            Long submittedChoiceId
    ) {
    }

    record Result(
            boolean isCorrect,
            String explanation,
            Integer currentProgress,
            Integer totalQuestion,
            Long correctChoiceId
    ) {
    }
}