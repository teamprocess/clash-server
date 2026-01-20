package com.process.clash.adapter.web.compete.rival.battle.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.battle.data.ModifyBattleData;

public class ModifyBattleDto {

    public record Request(
            Long battleId
    ) {

        public ModifyBattleData.Command toCommand(Actor actor) {

            return new ModifyBattleData.Command(
                    actor,
                    battleId
            );
        }
    }
}
