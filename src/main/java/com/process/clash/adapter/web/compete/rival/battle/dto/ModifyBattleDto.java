package com.process.clash.adapter.web.compete.rival.battle.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.battle.data.ModifyBattleData;
import io.swagger.v3.oas.annotations.media.Schema;

public class ModifyBattleDto {

    @Schema(name = "ModifyBattleRequestDocument")
    public record Request(
            Long id
    ) {

        public ModifyBattleData.Command toCommand(Actor actor) {

            return new ModifyBattleData.Command(
                    actor,
                    id
            );
        }
    }
}
