package com.process.clash.application.missions.data;

import com.process.clash.application.common.actor.Actor;

public class ResetMissionData {

    public record Command(
            Actor actor,
            Long missionId
    ) {
    }
}