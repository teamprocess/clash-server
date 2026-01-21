package com.process.clash.application.roadmap.chapter.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.roadmap.entity.*;

import java.util.List;

public class GetChapterDetailsData {

    public record Command(Actor actor, Long chapterId) {}

    public record Result(
            Long chapterId,
            String title,
            String description,
            Long currentMissionId,
            Long currentQuestionId,
            Integer currentQuestionIndex,
            Integer totalQuestions,
            List<MissionVo> missions
    ) {
        public static Result from(Chapter chapter, Long currentMissionId, Long currentQuestionId, Integer currentQuestionIndex, Integer totalQuestions) {
            List<MissionVo> missionVos = chapter.getMissions() != null
                    ? chapter.getMissions().stream()
                            .map(MissionVo::from)
                            .toList()
                    : List.of();

            return new Result(
                    chapter.getId(),
                    chapter.getTitle(),
                    chapter.getDescription(),
                    currentMissionId,
                    currentQuestionId,
                    currentQuestionIndex,
                    totalQuestions,
                    missionVos
            );
        }

        public record MissionVo(
                Long id,
                String title,
                List<QuestionVo> questions
        ) {
            public static MissionVo from(Mission mission) {
                List<QuestionVo> questionVos = mission.getQuestions() != null
                        ? mission.getQuestions().stream()
                                .map(QuestionVo::from)
                                .toList()
                        : List.of();

                return new MissionVo(
                        mission.getId(),
                        mission.getTitle(),
                        questionVos
                );
            }
        }

        public record QuestionVo(
                Long id,
                String content,
                List<ChoiceVo> choices
        ) {
            public static QuestionVo from(MissionQuestion question) {
                List<ChoiceVo> choiceVos = question.getChoices() != null
                        ? question.getChoices().stream()
                                .map(ChoiceVo::from)
                                .toList()
                        : List.of();

                return new QuestionVo(
                        question.getId(),
                        question.getContent(),
                        choiceVos
                );
            }
        }

        public record ChoiceVo(
                Long id,
                String content
        ) {
            public static ChoiceVo from(Choice choice) {
                return new ChoiceVo(
                        choice.getId(),
                        choice.getContent()
                );
            }
        }
    }
}
