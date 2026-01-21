package com.process.clash.adapter.web.compete.rival.rival.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.rival.data.ModifyRivalData;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class ModifyRivalDto {

    @Schema(name = "ModifyRivalRequestDoc")
    public record Request(
            @NotNull(message = "라이벌 아이디는 필수 입력값입니다.")
            Long id
    ) {

        public ModifyRivalData.Command toCommand(Actor actor) {

            return new ModifyRivalData.Command(
                    actor,
                    id
            );
        }
    }
}
