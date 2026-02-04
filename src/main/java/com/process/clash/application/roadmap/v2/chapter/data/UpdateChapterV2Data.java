package com.process.clash.application.roadmap.v2.chapter.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.roadmap.v2.entity.ChapterV2;

public class UpdateChapterV2Data {

    public record Command(
            Actor actor,
            Long chapterId,
            String title,
            String description,
            Integer orderIndex
    ) {
    }

    public record Result(
            Long chapterId,
            String title,
            String description,
            Integer orderIndex
    ) {
        public static Result from(ChapterV2 chapter) {
            return new Result(
                    chapter.getId(),
                    chapter.getTitle(),
                    chapter.getDescription(),
                    chapter.getOrderIndex()
            );
        }
    }
}
