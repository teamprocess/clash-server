package com.process.clash.adapter.web.record.v2.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.UpdateTaskCompletionV2Data;
import com.process.clash.domain.record.v2.entity.RecordTaskV2;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class UpdateTaskCompletionV2Dto {

    @Schema(name = "UpdateTaskCompletionV2DtoRequest")
    public record Request(
        @NotNull(message = "completed는 필수 입력값입니다.")
        Boolean completed
    ) {
        public UpdateTaskCompletionV2Data.Command toCommand(Actor actor, Long taskId) {
            return new UpdateTaskCompletionV2Data.Command(actor, taskId, completed);
        }
    }

    @Schema(name = "UpdateTaskCompletionV2DtoResponse")
    public record Response(
        Long id,
        Long subjectId,
        String name,
        boolean completed
    ) {
        public static Response from(UpdateTaskCompletionV2Data.Result result) {
            RecordTaskV2 task = result.task();
            return new Response(
                task.id(),
                task.subjectId(),
                task.name(),
                task.completed()
            );
        }
    }
}
