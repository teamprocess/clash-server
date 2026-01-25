package com.process.clash.adapter.web.ranking.dto;

import com.process.clash.application.ranking.data.GetChapterRankingData;

import java.util.List;

public class GetChapterRankingDto {

    public record Response(
        MyRankingVo myRank,
        List<RankersVo> allRankers
    ) {
        public static Response from(GetChapterRankingData.Result result) {
            MyRankingVo myRank = result.myRank() != null
                    ? MyRankingVo.from(result.myRank())
                    : null;

            List<RankersVo> allRankers = result.allRankers().stream()
                    .map(RankersVo::from)
                    .toList();

            return new Response(myRank, allRankers);
        }
    }

    public record MyRankingVo(
            Integer rank,
            Integer completedChaptersCount,
            Long id,
            String name,
            String profileImage
    ) {
        public static MyRankingVo from(GetChapterRankingData.MyRankingVo vo) {
            return new MyRankingVo(
                    vo.rank(),
                    vo.completedChaptersCount(),
                    vo.id(),
                    vo.name(),
                    vo.profileImage()
            );
        }
    }

    public record RankersVo(
            Integer rank,
            Integer completedChaptersCount,
            Long id,
            String name,
            String profileImage
    ) {
        public static RankersVo from(GetChapterRankingData.RankersVo vo) {
            return new RankersVo(
                    vo.rank(),
                    vo.completedChaptersCount(),
                    vo.id(),
                    vo.name(),
                    vo.profileImage()
            );
        }
    }
}
