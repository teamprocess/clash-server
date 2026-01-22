package com.process.clash.adapter.web.ranking.dto;

import java.util.List;

public class GetChapterRankingDto {

    public record Response(
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
