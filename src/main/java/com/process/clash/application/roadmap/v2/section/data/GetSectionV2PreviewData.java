package com.process.clash.application.roadmap.v2.section.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.roadmap.entity.Section;
import com.process.clash.domain.roadmap.entity.SectionKeyPoint;
import com.process.clash.domain.roadmap.v2.entity.ChapterV2;

import java.util.List;

public class GetSectionV2PreviewData {

    public record Command(Actor actor, Long sectionId) {}

    public record Result(
            Long id,
            String title,
            String description,
            Long totalChapters,
            List<ChapterVo> chapters,
            List<String> keyPoints
    ) {
        public static Result from(Section section, List<ChapterV2> chapters) {
            List<ChapterVo> chapterVos = chapters.stream()
                    .map(ChapterVo::from)
                    .toList();

            List<String> keyPointContents = section.getKeyPoints() != null
                    ? section.getKeyPoints().stream()
                            .map(SectionKeyPoint::getContent)
                            .toList()
                    : List.of();

            return new Result(
                    section.getId(),
                    section.getTitle(),
                    section.getDescription(),
                    (long) chapterVos.size(),
                    chapterVos,
                    keyPointContents
            );
        }

        public record ChapterVo(
                Long id,
                String title,
                String description
        ) {
            public static ChapterVo from(ChapterV2 chapter) {
                return new ChapterVo(chapter.getId(), chapter.getTitle(), chapter.getDescription());
            }
        }
    }
}
