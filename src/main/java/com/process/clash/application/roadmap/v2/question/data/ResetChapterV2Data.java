package com.process.clash.application.roadmap.v2.question.data;

import com.process.clash.application.common.actor.Actor;

public class ResetChapterV2Data {

    public record Command(
            Actor actor,
            Long chapterId
    ) {
    }
}
