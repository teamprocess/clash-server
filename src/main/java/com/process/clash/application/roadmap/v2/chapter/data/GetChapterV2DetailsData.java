package com.process.clash.application.roadmap.v2.chapter.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.roadmap.v2.entity.ChapterV2;
import com.process.clash.domain.roadmap.v2.entity.ChoiceV2;
import com.process.clash.domain.roadmap.v2.entity.QuestionV2;

import java.util.List;

public class GetChapterV2DetailsData {

    public record Command(
            Actor actor,
            Long chapterId
    ) {
    }

    public record Result(
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
        public static Result from(ChapterV2 chapter, Integer currentQuestionIndex, Integer correctCount, boolean isCleared) {
            List<QuestionInfo> questionInfos = chapter.getQuestions().stream()
                    .map(QuestionInfo::from)
                    .toList();

            return new Result(
                    chapter.getId(),
                    chapter.getTitle(),
                    chapter.getDescription(),
                    chapter.getOrderIndex(),
                    chapter.getStudyMaterialUrl(),
                    questionInfos,
                    questionInfos.size(),
                    currentQuestionIndex,
                    correctCount,
                    isCleared
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
        public static QuestionInfo from(QuestionV2 question) {
            List<ChoiceInfo> choiceInfos = question.getChoices().stream()
                    .map(ChoiceInfo::from)
                    .toList();

            return new QuestionInfo(
                    question.getId(),
                    question.getContent(),
                    question.getExplanation(),
                    question.getOrderIndex(),
                    question.getDifficulty(),
                    choiceInfos
            );
        }
    }

    public record ChoiceInfo(
            Long choiceId,
            String content
    ) {
        public static ChoiceInfo from(ChoiceV2 choice) {
            return new ChoiceInfo(
                    choice.getId(),
                    choice.getContent()
            );
        }
    }
}
