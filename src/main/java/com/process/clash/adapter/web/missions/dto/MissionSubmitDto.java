package com.process.clash.adapter.web.missions.dto;

public class MissionSubmitDto {

    public record Request(
            Long submittedChoiceId
    ) {
    }

    public record Response(
            boolean isCorrect,
            String explanation,
            Integer currentProgress,
            Integer totalQuestion,
            Long correctChoiceId
    ) {
    }
}