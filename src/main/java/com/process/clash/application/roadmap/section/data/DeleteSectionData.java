package com.process.clash.application.roadmap.section.data;

import com.process.clash.application.common.actor.Actor;
import lombok.AllArgsConstructor;

public class DeleteSectionData {

    @AllArgsConstructor
    public static class Command {
        private final Actor actor;
        private final Long sectionId;

        public Long getSectionId() {
            return sectionId;
        }
    }
}
