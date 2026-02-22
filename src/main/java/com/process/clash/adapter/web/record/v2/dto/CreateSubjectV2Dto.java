package com.process.clash.adapter.web.record.v2.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.CreateSubjectV2Data;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class CreateSubjectV2Dto {

    @Schema(name = "CreateSubjectV2DtoRequest")
    public record Request(
        @NotBlank(message = "name은 필수 입력값입니다.")
        String name
    ) {
        public CreateSubjectV2Data.Command toCommand(Actor actor) {
            return new CreateSubjectV2Data.Command(actor, name);
        }
    }
}
