package com.process.clash.application.missions.port.in;

public interface SubmitMissionAnswerUseCase {

    Result execute(Command command);

    record Command(
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