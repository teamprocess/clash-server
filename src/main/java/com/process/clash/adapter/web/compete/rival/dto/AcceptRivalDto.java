package com.process.clash.adapter.web.compete.rival.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.data.AcceptRivalData;
import jakarta.validation.constraints.NotNull;

public class AcceptRivalDto {

    public record Request(
            @NotNull(message = "라이벌 아이디는 필수 입력값입니다.")
            Long id
    ) {

        public AcceptRivalData.Command toCommand(Actor actor) {

            return new AcceptRivalData.Command(
                    actor,
                    id
            );
        }
    }
}
