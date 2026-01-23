package com.process.clash.adapter.web.ranking.dto;

import com.process.clash.application.ranking.data.GetChapterRankingData;

import java.util.List;

public class GetChapterRankingDto {

    public record Response(
        MyRankingVo myRank,
        List<RankersVo> allRankers
    ) {
        public static Response from(GetChapterRankingData.Result result) {

            return new Response(
                    result.myRank(),
                    result.allRankers()
            );
        }
    }

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
