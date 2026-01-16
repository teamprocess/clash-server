package com.process.clash.application.roadmap.section.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.roadmap.entity.Chapter;
import com.process.clash.domain.roadmap.entity.Section;
import com.process.clash.domain.roadmap.entity.SectionKeyPoint;

import java.util.List;

public class GetSectionPreviewData {

    public record Command(Actor actor, Long sectionId) {}

    public record Result(
            Long id,
            String title,
            String description,
            Long totalChapters,
            List<ChapterVo> chapters,
            List<String> keyPoints
    ) {
        public static Result from(Section section, Long totalSteps) {
            List<ChapterVo> chapterVos = section.getChapters() != null
                    ? section.getChapters().stream()
                            .map(ChapterVo::from)
                            .toList()
                    : List.of();

            List<String> keyPointContents = section.getKeyPoints() != null
                    ? section.getKeyPoints().stream()
                            .map(SectionKeyPoint::getContent)
                            .toList()
                    : List.of();

            return new Result(
                    section.getId(),
                    section.getTitle(),
                    section.getDescription(),
                    totalSteps,
                    chapterVos,
                    keyPointContents
            );
        }

        public record ChapterVo(
                Long id,
                String title,
                String description
        ) {
            public static ChapterVo from(Chapter chapter) {
                return new ChapterVo(
                        chapter.getId(),
                        chapter.getTitle(),
                        chapter.getDescription()
                );
            }
        }
    }
}
