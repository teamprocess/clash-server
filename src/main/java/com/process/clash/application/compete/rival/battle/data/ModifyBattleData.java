package com.process.clash.application.compete.rival.battle.data;

import com.process.clash.application.common.actor.Actor;

public class ModifyBattleData {

    public record Command(
            Actor actor,
            Long id
    ) {
    }
}
