package com.process.clash.application.roadmap.section.data;

import com.process.clash.application.common.actor.Actor;

public class DeleteSectionData {

    public record Command(Actor actor, Long sectionId) {}
}
