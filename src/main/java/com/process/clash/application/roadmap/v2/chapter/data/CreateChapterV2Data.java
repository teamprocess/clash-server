package com.process.clash.application.roadmap.v2.chapter.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.roadmap.v2.entity.ChapterV2;

import java.util.List;

public class CreateChapterV2Data {

    public record Command(
            Actor actor,
            Long sectionId,
            String title,
            String description,
            Integer orderIndex
    ) {
        public ChapterV2 toDomain() {
            return new ChapterV2(
                    null,
                    sectionId,
                    title,
                    description,
                    orderIndex,
                    List.of()
            );
        }
    }

    public record Result(
            Long chapterId,
            Long sectionId,
            String title,
            String description,
            Integer orderIndex
    ) {
        public static Result from(ChapterV2 chapter) {
            return new Result(
                    chapter.getId(),
                    chapter.getSectionId(),
                    chapter.getTitle(),
                    chapter.getDescription(),
                    chapter.getOrderIndex()
            );
        }
    }
}
