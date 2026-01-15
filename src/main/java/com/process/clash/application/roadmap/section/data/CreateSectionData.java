package com.process.clash.application.roadmap.section.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.roadmap.entity.Section;

import java.util.List;

public class CreateSectionData {

    public record Command(
            Actor actor,
            Major major,
            String title,
            String category,
            String description,
            List<String> keyPoints
    ) {
        public Section toDomain() {
            return new Section(null, major, title, description, category, List.of(), List.of(), null, null);
        }
    }

    public record Result(
            Long sectionId,
            String major,
            String title,
            String category,
            String description,
            List<String> keyPoints,
            String createdAt
    ) {
        public static Result from(Section section, List<String> keyPoints) {
            String createdAtString = section.getCreatedAt() != null
                    ? section.getCreatedAt().toString()
                    : null;

            return new Result(
                    section.getId(),
                    section.getMajor().name(),
                    section.getTitle(),
                    section.getCategory(),
                    section.getDescription(),
                    keyPoints,
                    createdAtString
            );
        }
    }
}
