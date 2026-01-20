package com.process.clash.application.roadmap.section.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.roadmap.entity.Section;

import java.util.List;

public class GetSectionsData {

    public record Command(Actor actor, Major major) {}

    public record Result(
            List<SectionVo> sections,
            List<String> categories,
            Integer completedSections,
            Integer totalSections
    ) {
        public record SectionVo(
                Long id,
                String title,
                String category,
                Boolean completed,
                Boolean locked
        ) {
            public static SectionVo from(Section section, Boolean completed, Boolean locked) {
                return new SectionVo(
                        section.getId(),
                        section.getTitle(),
                        section.getCategory().getName(),
                        completed,
                        locked
                );
            }
        }
    }
}
