package com.process.clash.application.roadmap.section.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.roadmap.entity.*;

import java.util.List;
import java.util.Map;

public class GetSectionDetailsData {

    public record Command(Actor actor, Long sectionId) {}

    public record Result(
            Long sectionId,
            String sectionTitle,
            Integer totalChapters,
            Long currentChapterId,
            Integer currentOrderIndex,
            Integer currentMissionIndex,
            List<ChapterVo> chapters
    ) {
        public static Result from(Section section, Long currentChapterId, Integer currentOrderIndex, Integer currentMissionIndex, Map<Long, List<Mission>> chapterMissionsMap, Map<Long, UserMissionHistory> missionHistoryMap) {
            List<ChapterVo> chapterVos = section.getChapters() != null
                    ? section.getChapters().stream()
                            .map(chapter -> {
                                List<Mission> missions = chapterMissionsMap.getOrDefault(chapter.getId(), List.of());
                                Integer totalMissions = missions.size();
                                Integer completedMissions = (int) missions.stream()
                                        .filter(mission -> {
                                            UserMissionHistory history = missionHistoryMap.get(mission.getId());
                                            return history != null && history.isCleared();
                                        })
                                        .count();
                                return ChapterVo.from(chapter, completedMissions, totalMissions);
                            })
                            .toList()
                    : List.of();

            return new Result(
                    section.getId(),
                    section.getTitle(),
                    chapterVos.size(),
                    currentChapterId,
                    currentOrderIndex,
                    currentMissionIndex,
                    chapterVos
            );
        }

        public record ChapterVo(
                Long id,
                String title,
                Integer orderIndex,
                Integer completedMissions,
                Integer totalMissions
        ) {
            public static ChapterVo from(Chapter chapter, Integer completedMissions, Integer totalMissions) {
                return new ChapterVo(
                        chapter.getId(),
                        chapter.getTitle(),
                        chapter.getOrderIndex(),
                        completedMissions,
                        totalMissions
                );
            }
        }

        public record MissionVo(
                Long id,
                String title,
                Integer orderIndex,
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
                        mission.getOrderIndex(),
                        questionVos
                );
            }
        }

        public record QuestionVo(
                Long id,
                String title,
                Integer orderIndex,
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
                        question.getOrderIndex(),
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
