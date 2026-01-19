package com.process.clash.adapter.web.major.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.major.data.UpdateMajorQuestionData;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

public class UpdateMajorQuestionDto {

    @Schema(name = "UpdateMajorQuestionDtoRequest")

    public record Request(
            String content,
            @Valid
            MajorWeightVo weight
    ) {
        public UpdateMajorQuestionData.Command toCommand(Actor actor, Long questionId) {
            UpdateMajorQuestionData.Command.WeightVo weightVo = null;
            if (weight != null) {
                weightVo = new UpdateMajorQuestionData.Command.WeightVo(
                        weight.web(),
                        weight.app(),
                        weight.server(),
                        weight.ai(),
                        weight.game()
                );
            }
            return new UpdateMajorQuestionData.Command(
                    actor,
                    questionId,
                    content,
                    weightVo
            );
        }
    }

    @Schema(name = "UpdateMajorQuestionDtoResponse")

    public record Response(
            Long questionId,
            String content,
            MajorWeightVo weight,
            LocalDateTime updatedAt
    ) {
        public static Response from(UpdateMajorQuestionData.Result result) {
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
                    result.updatedAt()
            );
        }
    }


    public record MajorWeightVo(
            Integer web,
            Integer app,
            Integer server,
            Integer ai,
            Integer game
    ) {}
}
