package com.process.clash.application.ranking.data;

import com.process.clash.application.common.actor.Actor;

import java.util.List;

public class GetChapterRankingData {

    public record Command(
            Actor actor
    ) {
        public static Command from(Actor actor) {
            return new Command(actor);
        }
    }

    public record Result(
        MyRankingVo myRank,
        List<RankersVo> allRankers
    ) {}

    public record MyRankingVo(
            Integer rank,
            Integer completedChaptersCount,
            Long id,
            String name,
            String profileImage
    ) {}

    public record RankersVo(
            Integer rank,
            Integer completedChaptersCount,
            Long id,
            String name,
            String profileImage
    ) {}
}
