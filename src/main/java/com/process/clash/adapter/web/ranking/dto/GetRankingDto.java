package com.process.clash.adapter.web.ranking.dto;

import com.process.clash.application.ranking.data.GetRankingData;
import com.process.clash.application.ranking.data.UserRanking;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public class GetRankingDto {

    @Schema(name = "GetRankingDtoResponse")

    public record Response(
            String category,
            String period,
            List<UserRanking> rankings
    ) {

        public static Response from(GetRankingData.Result result) {

            return new Response(
                    result.category(),
                    result.period(),
                    result.rankings()
            );
        }
    }
}
