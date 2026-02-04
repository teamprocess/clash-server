package com.process.clash.adapter.web.roadmap.v2.question.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.v2.question.data.CreateQuestionV2Data;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateQuestionV2Dto {

    @Schema(name = "CreateQuestionV2DtoRequest")
    public record Request(
            @NotNull(message = "chapterId는 필수 입력값입니다.")
            Long chapterId,

            @NotBlank(message = "content는 필수 입력값입니다.")
            String content,

            String explanation,

            @NotNull(message = "orderIndex는 필수 입력값입니다.")
            Integer orderIndex,

            @NotNull(message = "difficulty는 필수 입력값입니다.")
            Integer difficulty
    ) {
        public CreateQuestionV2Data.Command toCommand(Actor actor) {
            return new CreateQuestionV2Data.Command(
                    actor,
                    chapterId,
                    content,
                    explanation,
                    orderIndex,
                    difficulty
            );
        }
    }

    @Schema(name = "CreateQuestionV2DtoResponse")
    public record Response(
            Long questionId,
            Long chapterId,
            String content,
            String explanation,
            Integer orderIndex,
            Integer difficulty
    ) {
        public static Response from(CreateQuestionV2Data.Result result) {
            return new Response(
                    result.questionId(),
                    result.chapterId(),
                    result.content(),
                    result.explanation(),
                    result.orderIndex(),
                    result.difficulty()
            );
        }
    }
}
