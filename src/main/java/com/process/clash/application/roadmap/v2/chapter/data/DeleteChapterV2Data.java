package com.process.clash.application.roadmap.v2.chapter.data;

import com.process.clash.application.common.actor.Actor;

public class DeleteChapterV2Data {

    public record Command(
            Actor actor,
            Long chapterId
    ) {
    }
}
