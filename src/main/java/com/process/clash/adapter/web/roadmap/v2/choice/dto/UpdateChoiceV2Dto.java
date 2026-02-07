package com.process.clash.adapter.web.roadmap.v2.choice.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.v2.choice.data.UpdateChoiceV2Data;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateChoiceV2Dto {

    @Schema(name = "UpdateChoiceV2DtoRequest")
    public record Request(
            @NotBlank(message = "content는 필수 입력값입니다.")
            String content,

            @NotNull(message = "isCorrect는 필수 입력값입니다.")
            Boolean isCorrect,

            @NotNull(message = "orderIndex는 필수 입력값입니다.")
            Integer orderIndex
    ) {
        public UpdateChoiceV2Data.Command toCommand(Actor actor, Long choiceId) {
            return new UpdateChoiceV2Data.Command(
                    actor,
                    choiceId,
                    content,
                    isCorrect,
                    orderIndex
            );
        }
    }

    @Schema(name = "UpdateChoiceV2DtoResponse")
    public record Response(
            Long choiceId,
            String content,
            boolean isCorrect,
            Integer orderIndex
    ) {
        public static Response from(UpdateChoiceV2Data.Result result) {
            return new Response(
                    result.choiceId(),
                    result.content(),
                    result.isCorrect(),
                    result.orderIndex()
            );
        }
    }
}
