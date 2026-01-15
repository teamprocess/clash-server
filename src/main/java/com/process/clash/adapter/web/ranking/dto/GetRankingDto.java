package com.process.clash.adapter.web.ranking.dto;

import com.process.clash.application.ranking.data.GetRankingData;
import com.process.clash.domain.common.enums.PeriodCategory;
import com.process.clash.domain.common.enums.TargetCategory;

import java.util.List;

public class GetRankingDto {

    public record Response(
            TargetCategory category,
            PeriodCategory period,
            List<Ranking> rankings
    ) {

        public static Response from(GetRankingData.Result result) {

            return new Response(
                    result.category(),
                    result.period(),
                    result.rankings().stream()
                            .map(data -> new Ranking(
                                    data.name(),
                                    data.username(),
                                    data.profileImage(),
                                    data.isRival(),
                                    data.linkedId(),
                                    data.point()
                            ))
                    .toList()
            );
        }
    }

    private record Ranking(
            String name,
            String username,
            String profileImage,
            Boolean isRival,
            String linkedId,
            Integer point
    ) {}
}
