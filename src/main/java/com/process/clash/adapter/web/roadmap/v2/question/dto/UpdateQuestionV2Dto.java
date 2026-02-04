package com.process.clash.adapter.web.roadmap.v2.question.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.v2.question.data.UpdateQuestionV2Data;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateQuestionV2Dto {

    @Schema(name = "UpdateQuestionV2DtoRequest")
    public record Request(
            @NotBlank(message = "content는 필수 입력값입니다.")
            String content,

            String explanation,

            @NotNull(message = "orderIndex는 필수 입력값입니다.")
            Integer orderIndex,

            @NotNull(message = "difficulty는 필수 입력값입니다.")
            Integer difficulty
    ) {
        public UpdateQuestionV2Data.Command toCommand(Actor actor, Long questionId) {
            return new UpdateQuestionV2Data.Command(
                    actor,
                    questionId,
                    content,
                    explanation,
                    orderIndex,
                    difficulty
            );
        }
    }

    @Schema(name = "UpdateQuestionV2DtoResponse")
    public record Response(
            Long questionId,
            String content,
            String explanation,
            Integer orderIndex,
            Integer difficulty
    ) {
        public static Response from(UpdateQuestionV2Data.Result result) {
            return new Response(
                    result.questionId(),
                    result.content(),
                    result.explanation(),
                    result.orderIndex(),
                    result.difficulty()
            );
        }
    }
}
