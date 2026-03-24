package com.process.clash.adapter.web.compete.my.dto;

import com.process.clash.application.compete.my.data.AnalyzeMyPeriodData;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class AnalyzeMyPeriodDto {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd");

    @Schema(name = "AnalyzeMyPeriodDtoResponse")
    public record Response(
            String category,
            String period,
            List<DataPointDto> dataPoints
    ) {

        public static Response from(AnalyzeMyPeriodData.Result result) {
            return new Response(
                    result.category(),
                    result.period(),
                    result.dataPoints().stream()
                            .map(dp -> new DataPointDto(dp.date().format(DATE_FORMATTER), dp.point()))
                            .toList()
            );
        }
    }

    public record DataPointDto(
            String date,
            Double point
    ) {}
}