package com.process.clash.adapter.web.compete.my.dto;

import com.process.clash.application.compete.my.data.AnalyzeMyActivityData;
import com.process.clash.application.compete.my.data.Streak;
import com.process.clash.application.compete.my.data.Variation;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public class AnalyzeMyActivityDto {

    @Schema(name = "AnalyzeMyActivityDtoResponse")

    public record Response(
            String category,
            List<Streak> streaks,
            List<Variation> variations
    ) {

        public static Response from(AnalyzeMyActivityData.Result result) {

            return new Response(
                    result.category(),
                    result.streaks(),
                    result.variations()
            );
        }
    }
}
