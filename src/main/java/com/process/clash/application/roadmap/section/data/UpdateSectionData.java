package com.process.clash.application.roadmap.section.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.roadmap.entity.Section;

import java.util.List;

public class UpdateSectionData {

    public record Command(
            Actor actor,
            Long sectionId,
            String title,
            Long categoryId,
            String description,
            Integer orderIndex,
            List<String> keyPoints,
            List<Long> prerequisiteSectionIds
    ) {}

    public record Result(
            Long sectionId,
            String title,
            Long categoryId,
            String description,
            List<String> keyPoints,
            String updatedAt
    ) {
        public static Result from(Section section, List<String> keyPoints) {
            String updatedAtString = section.getUpdatedAt() != null
                    ? section.getUpdatedAt().toString()
                    : null;

            return new Result(
                    section.getId(),
                    section.getTitle(),
                    section.getCategory().getId(),
                    section.getDescription(),
                    keyPoints,
                    updatedAtString
            );
        }
    }
}
