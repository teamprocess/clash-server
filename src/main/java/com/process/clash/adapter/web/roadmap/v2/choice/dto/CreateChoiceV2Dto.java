package com.process.clash.adapter.web.roadmap.v2.choice.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.v2.choice.data.CreateChoiceV2Data;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateChoiceV2Dto {

    @Schema(name = "CreateChoiceV2DtoRequest")
    public record Request(
            @NotNull(message = "questionId는 필수 입력값입니다.")
            Long questionId,

            @NotBlank(message = "content는 필수 입력값입니다.")
            String content,

            @NotNull(message = "isCorrect는 필수 입력값입니다.")
            Boolean isCorrect
    ) {
        public CreateChoiceV2Data.Command toCommand(Actor actor) {
            return new CreateChoiceV2Data.Command(
                    actor,
                    questionId,
                    content,
                    isCorrect
            );
        }
    }

    @Schema(name = "CreateChoiceV2DtoResponse")
    public record Response(
            Long choiceId,
            Long questionId,
            String content,
            boolean isCorrect
    ) {
        public static Response from(CreateChoiceV2Data.Result result) {
            return new Response(
                    result.choiceId(),
                    result.questionId(),
                    result.content(),
                    result.isCorrect()
            );
        }
    }
}
