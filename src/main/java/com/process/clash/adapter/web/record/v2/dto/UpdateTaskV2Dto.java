package com.process.clash.adapter.web.record.v2.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.UpdateTaskV2Data;
import com.process.clash.domain.record.v2.entity.RecordTaskV2;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class UpdateTaskV2Dto {

    @Schema(name = "UpdateTaskV2DtoRequest")
    public record Request(
        Long subjectId,
        @NotBlank(message = "name은 필수 입력값입니다.")
        String name
    ) {
        public UpdateTaskV2Data.Command toCommand(Actor actor, Long taskId) {
            return new UpdateTaskV2Data.Command(actor, taskId, subjectId, name);
        }
    }

    @Schema(name = "UpdateTaskV2DtoResponse")
    public record Response(
        Long id,
        Long subjectId,
        String name,
        boolean completed,
        Long studyTime
    ) {
        public static Response from(UpdateTaskV2Data.Result result) {
            RecordTaskV2 task = result.task();
            return new Response(
                task.id(),
                task.subjectId(),
                task.name(),
                task.completed(),
                task.studyTime()
            );
        }
    }
}
