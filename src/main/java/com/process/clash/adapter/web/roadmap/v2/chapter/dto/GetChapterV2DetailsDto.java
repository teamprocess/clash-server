package com.process.clash.adapter.web.roadmap.v2.chapter.dto;

import com.process.clash.application.roadmap.v2.chapter.data.GetChapterV2DetailsData;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class GetChapterV2DetailsDto {

    @Schema(name = "GetChapterV2DetailsDtoResponse")
    public record Response(
            Long chapterId,
            String title,
            String description,
            Integer orderIndex,
            String studyMaterialUrl,
            List<QuestionInfo> questions,
            Integer totalQuestions,
            Integer currentQuestionIndex,
            Integer correctCount,
            boolean isCleared
    ) {
        public static Response from(GetChapterV2DetailsData.Result result) {
            return new Response(
                    result.chapterId(),
                    result.title(),
                    result.description(),
                    result.orderIndex(),
                    result.studyMaterialUrl(),
                    result.questions().stream().map(QuestionInfo::from).toList(),
                    result.totalQuestions(),
                    result.currentQuestionIndex(),
                    result.correctCount(),
                    result.isCleared()
            );
        }
    }

    public record QuestionInfo(
            Long questionId,
            String content,
            String explanation,
            Integer orderIndex,
            Integer difficulty,
            List<ChoiceInfo> choices
    ) {
        public static QuestionInfo from(GetChapterV2DetailsData.QuestionInfo info) {
            return new QuestionInfo(
                    info.questionId(),
                    info.content(),
                    info.explanation(),
                    info.orderIndex(),
                    info.difficulty(),
                    info.choices().stream().map(ChoiceInfo::from).toList()
            );
        }
    }

    public record ChoiceInfo(
            Long choiceId,
            String content
    ) {
        public static ChoiceInfo from(GetChapterV2DetailsData.ChoiceInfo info) {
            return new ChoiceInfo(
                    info.choiceId(),
                    info.content()
            );
        }
    }
}
