package com.process.clash.adapter.web.record.v2.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.UpdateSubjectV2Data;
import com.process.clash.domain.record.v2.entity.RecordSubjectV2;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class UpdateSubjectV2Dto {

    @Schema(name = "UpdateSubjectV2DtoRequest")
    public record Request(
        @NotBlank(message = "name은 필수 입력값입니다.")
        String name
    ) {
        public UpdateSubjectV2Data.Command toCommand(Actor actor, Long subjectId) {
            return new UpdateSubjectV2Data.Command(actor, subjectId, name);
        }
    }

    @Schema(name = "UpdateSubjectV2DtoResponse")
    public record Response(
        Long id,
        String name,
        Long studyTime
    ) {
        public static Response from(UpdateSubjectV2Data.Result result) {
            RecordSubjectV2 subject = result.subject();
            return new Response(
                subject.id(),
                subject.name(),
                subject.studyTime()
            );
        }
    }
}
