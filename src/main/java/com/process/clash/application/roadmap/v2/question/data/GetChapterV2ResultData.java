package com.process.clash.application.roadmap.v2.question.data;

import com.process.clash.application.common.actor.Actor;

public class GetChapterV2ResultData {

    public record Command(
            Actor actor,
            Long chapterId
    ) {
    }

    public record Result(
            boolean isCleared,
            Integer correctCount,
            Integer totalCount,
            Integer scorePercentage
    ) {
    }
}
