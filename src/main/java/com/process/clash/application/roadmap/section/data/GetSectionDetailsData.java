package com.process.clash.application.roadmap.section.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.roadmap.entity.*;

import java.util.List;

public class GetSectionDetailsData {

    public record Command(Actor actor, Long sectionId) {}

    public record Result(
            Long sectionId,
            String sectionTitle,
            Integer totalChapters,
            Integer currentChapters,
            List<ChapterVo> chapters
    ) {
        public static Result from(Section section, Integer currentChapters) {
            List<ChapterVo> chapterVos = section.getChapters() != null
                    ? section.getChapters().stream()
                            .map(chapter -> ChapterVo.from(chapter, 1)) // difficulty는 서비스에서 계산 필요
                            .toList()
                    : List.of();

            return new Result(
                    section.getId(),
                    section.getTitle(),
                    chapterVos.size(),
                    currentChapters,
                    chapterVos
            );
        }

        public record ChapterVo(
                Long id,
                String title,
                Integer difficulty,
                List<MissionVo> missions
        ) {
            public static ChapterVo from(Chapter chapter, Integer difficulty) {
                List<MissionVo> missionVos = chapter.getMissions() != null
                        ? chapter.getMissions().stream()
                                .map(MissionVo::from)
                                .toList()
                        : List.of();

                return new ChapterVo(
                        chapter.getId(),
                        chapter.getTitle(),
                        difficulty,
                        missionVos
                );
            }
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
                String title,
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
