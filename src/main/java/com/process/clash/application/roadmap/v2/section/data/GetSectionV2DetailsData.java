package com.process.clash.application.roadmap.v2.section.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.roadmap.entity.Section;
import com.process.clash.domain.roadmap.v2.entity.ChapterV2;

import java.util.List;

public class GetSectionV2DetailsData {

    public record Command(Actor actor, Long sectionId) {}

    public record Result(
            Long sectionId,
            String sectionTitle,
            Boolean completed,
            Integer totalChapters,
            Long currentChapterId,
            Integer currentOrderIndex,
            List<ChapterVo> chapters
    ) {
        public static Result from(
                Section section,
                Boolean completed,
                Long currentChapterId,
                Integer currentOrderIndex,
                List<ChapterV2> chapters
        ) {
            List<ChapterVo> chapterVos = chapters.stream()
                    .map(ChapterVo::from)
                    .toList();

            return new Result(
                    section.getId(),
                    section.getTitle(),
                    completed,
                    chapterVos.size(),
                    currentChapterId,
                    currentOrderIndex,
                    chapterVos
            );
        }

        public record ChapterVo(
                Long chapterId,
                String title,
                String description,
                Integer orderIndex,
                String studyMaterialUrl
        ) {
            public static ChapterVo from(ChapterV2 chapter) {
                return new ChapterVo(
                        chapter.getId(),
                        chapter.getTitle(),
                        chapter.getDescription(),
                        chapter.getOrderIndex(),
                        chapter.getStudyMaterialUrl()
                );
            }
        }
    }
}
