package com.process.clash.application.roadmap.section.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.roadmap.entity.Section;

import java.util.List;

public class UpdateSectionData {

    public record Command(
            Actor actor,
            Long sectionId,
            String title,
            String category,
            String description,
            Integer orderIndex,
            List<Long> prerequisiteSectionIds
    ) {}

    public record Result(
            Long sectionId,
            String title,
            String category,
            String description,
            String updatedAt
    ) {
        public static Result from(Section section) {
            String updatedAtString = section.getUpdatedAt() != null
                    ? section.getUpdatedAt().toString()
                    : null;

            return new Result(
                    section.getId(),
                    section.getTitle(),
                    section.getCategory(),
                    section.getDescription(),
                    updatedAtString
            );
        }
    }
}
