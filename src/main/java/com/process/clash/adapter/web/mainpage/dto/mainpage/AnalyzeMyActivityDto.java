package com.process.clash.adapter.web.mainpage.dto.mainpage;

import com.process.clash.application.mainpage.data.mainpage.AnalyzeMyActivityData;
import com.process.clash.domain.common.enums.TargetCategory;
import com.process.clash.domain.common.enums.WeekCategory;

import java.time.LocalDate;
import java.util.List;

public class AnalyzeMyActivityDto {

    public record Response(
            TargetCategory category,
            List<Streak> streaks,
            List<Variation> variations
    ) {

        public static Response from(AnalyzeMyActivityData.Result result) {

            return new Response(
                    result.category(),
                    result.streaks().stream()
                            .map(data -> new Streak(
                                data.date(),
                                data.detailedInfo(),
                                data.dayOfTheWeek()
                            ))
                    .toList(),
                    result.variations().stream()
                            .map(data -> new Variation(
                                    data.month(),
                                    data.avgVariationPerMonth()
                            ))
                    .toList()
            );
        }
    }

    private record Streak(
            LocalDate date,
            Integer detailedInfo,
            WeekCategory dayOfTheWeek
    ) {}

    private record Variation(
            Integer month,
            Double avgVariationPerMonth
    ) {}
}
