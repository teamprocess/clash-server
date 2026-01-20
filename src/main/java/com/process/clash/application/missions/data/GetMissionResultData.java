package com.process.clash.application.missions.data;

import com.process.clash.application.common.actor.Actor;

public class GetMissionResultData {

    public record Command(
            Actor actor,
            Long missionId
    ) {
    }

    public record Result(
            Long missionId,
            Boolean isCleared,
            Integer correctCount,
            Integer totalCount,
            Long nextMissionId,
            Long nextStepId
    ) {
    }
}