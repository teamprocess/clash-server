package com.process.clash.adapter.web.compete.rival.battle.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.battle.data.ApplyBattleData;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class ApplyBattleDto {

    @Schema(name = "ApplyBattleRequestDoc")
    public record Request(
            @NotNull(message = "라이벌 아이디는 필수 입력값입니다.")
            Long id,
            @NotNull(message = "기간은 필수 입력값입니다.")
            Integer duration
    ) {

        public ApplyBattleData.Command toCommand(Actor actor) {

            return new ApplyBattleData.Command(
                    actor,
                    id,
                    duration
            );
        }
    }
}
