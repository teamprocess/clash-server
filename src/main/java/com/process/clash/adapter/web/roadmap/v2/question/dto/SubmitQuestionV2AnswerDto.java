package com.process.clash.adapter.web.roadmap.v2.question.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.v2.question.data.SubmitQuestionV2AnswerData;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class SubmitQuestionV2AnswerDto {

    @Schema(name = "SubmitQuestionV2AnswerDtoRequest")
    public record Request(
            @NotNull(message = "submittedChoiceId는 필수 입력값입니다.")
            Long submittedChoiceId
    ) {
        public SubmitQuestionV2AnswerData.Command toCommand(Actor actor, Long questionId) {
            return new SubmitQuestionV2AnswerData.Command(actor, questionId, submittedChoiceId);
        }
    }

    @Schema(name = "SubmitQuestionV2AnswerDtoResponse")
    public record Response(
            boolean isCorrect,
            String explanation,
            Integer currentProgress,
            Integer totalQuestion,
            Long correctChoiceId,
            boolean isChapterCleared,
            Long nextChapterId,
            Integer nextChapterOrderIndex
    ) {
        public static Response from(SubmitQuestionV2AnswerData.Result result) {
            return new Response(
                    result.isCorrect(),
                    result.explanation(),
                    result.currentProgress(),
                    result.totalQuestion(),
                    result.correctChoiceId(),
                    result.isChapterCleared(),
                    result.nextChapterId(),
                    result.nextChapterOrderIndex()
            );
        }
    }
}
