package com.process.clash.adapter.web.major.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.major.data.PostMajorQuestionData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

public class PostMajorQuestionDto {

    @Schema(name = "PostMajorQuestionDtoRequest")

    public record Request(
            @NotBlank(message = "질문 내용은 비워둘 수 없습니다.")
            String content,
            @NotNull(message = "가중치는 비워둘 수 없습니다.")
            @Valid
            MajorWeightVo weight
    ) {
        public PostMajorQuestionData.Command toCommand(Actor actor) {
            return new PostMajorQuestionData.Command(
                    actor,
                    content,
                    new PostMajorQuestionData.Command.WeightVo(
                            weight.web(),
                            weight.app(),
                            weight.server(),
                            weight.ai(),
                            weight.game()
                    )
            );
        }
    }

    @Schema(name = "PostMajorQuestionDtoResponse")

    public record Response(
            Long questionId,
            String content,
            MajorWeightVo weight,
            LocalDateTime createdAt
    ) {
        public static Response from(PostMajorQuestionData.Result result) {
            return new Response(
                    result.questionId(),
                    result.content(),
                    new MajorWeightVo(
                            result.weight().web(),
                            result.weight().app(),
                            result.weight().server(),
                            result.weight().ai(),
                            result.weight().game()
                    ),
                    result.createdAt()
            );
        }
    }

    public record MajorWeightVo(
            @NotNull(message = "web 가중치는 비워둘 수 없습니다.")
            Integer web,

            @NotNull(message = "app 가중치는 비워둘 수 없습니다.")
            Integer app,

            @NotNull(message = "server 가중치는 비워둘 수 없습니다.")
            Integer server,

            @NotNull(message = "ai 가중치는 비워둘 수 없습니다.")
            Integer ai,

            @NotNull(message = "game 가중치는 비워둘 수 없습니다.")
            Integer game
    ) {}
}
