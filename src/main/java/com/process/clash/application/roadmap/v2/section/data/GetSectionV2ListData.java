package com.process.clash.application.roadmap.v2.section.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.roadmap.entity.Section;
import com.process.clash.domain.roadmap.v2.entity.ChapterV2;

import java.util.List;

public class GetSectionV2ListData {

    public record Command(Actor actor, Major major) {}

    public record Result(
            List<SectionVo> sections,
            List<Long> categories,
            Integer completedSections,
            Integer totalSections
    ) {
        public record SectionVo(
                Long id,
                String title,
                Long categoryId,
                String categoryImageUrl,
                Boolean completed,
                Boolean locked,
                List<ChapterVo> chapters
        ) {
            public static SectionVo from(Section section, Boolean completed, Boolean locked, List<ChapterV2> chapters) {
                return new SectionVo(
                        section.getId(),
                        section.getTitle(),
                        section.getCategory().getId(),
                        section.getCategory().getImageUrl(),
                        completed,
                        locked,
                        chapters.stream().map(ChapterVo::from).toList()
                );
            }
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
