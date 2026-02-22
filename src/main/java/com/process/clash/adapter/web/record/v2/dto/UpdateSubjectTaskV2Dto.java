package com.process.clash.adapter.web.record.v2.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.UpdateSubjectTaskV2Data;
import com.process.clash.domain.record.v2.entity.RecordTaskV2;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class UpdateSubjectTaskV2Dto {

    @Schema(name = "UpdateSubjectTaskV2DtoRequest")
    public record Request(
        @NotBlank(message = "name은 필수 입력값입니다.")
        String name
    ) {
        public UpdateSubjectTaskV2Data.Command toCommand(
            Actor actor,
            Long subjectId,
            Long taskId
        ) {
            return new UpdateSubjectTaskV2Data.Command(actor, subjectId, taskId, name);
        }
    }

    @Schema(name = "UpdateSubjectTaskV2DtoResponse")
    public record Response(
        Long id,
        Long subjectId,
        String name,
        Long studyTime
    ) {
        public static Response from(UpdateSubjectTaskV2Data.Result result) {
            RecordTaskV2 task = result.task();
            return new Response(
                task.id(),
                task.subjectId(),
                task.name(),
                task.studyTime()
            );
        }
    }
}
