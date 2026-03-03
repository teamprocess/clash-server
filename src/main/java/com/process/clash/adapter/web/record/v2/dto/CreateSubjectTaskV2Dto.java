package com.process.clash.adapter.web.record.v2.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.CreateSubjectTaskV2Data;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class CreateSubjectTaskV2Dto {

    @Schema(name = "CreateSubjectTaskV2DtoRequest")
    public record Request(
        Long subjectId,
        @NotBlank(message = "name은 필수 입력값입니다.")
        String name
    ) {
        public CreateSubjectTaskV2Data.Command toCommand(Actor actor) {
            return new CreateSubjectTaskV2Data.Command(actor, subjectId, name);
        }
    }
}
