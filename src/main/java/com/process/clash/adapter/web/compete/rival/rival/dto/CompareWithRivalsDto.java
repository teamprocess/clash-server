package com.process.clash.adapter.web.compete.rival.rival.dto;

import com.process.clash.application.compete.rival.rival.data.CompareWithRivalsData;
import com.process.clash.application.compete.rival.rival.data.TotalData;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CompareWithRivalsDto {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd");

    public record Response(
            String category,
            String period,
            List<UserData> totalData
    ) {

        public static Response from(CompareWithRivalsData.Result result) {

            List<UserData> userData = result.totalData().stream()
                    .map(data -> new UserData(
                            data.id(),
                            data.name(),
                            data.dataPoint().stream()
                                    .map(dp -> new DataPointDto(
                                            dp.date().format(DATE_FORMATTER),
                                            dp.point()
                                    ))
                                    .toList()
                    ))
                    .toList();

            return new Response(
                    result.category(),
                    result.period(),
                    userData
            );
        }
    }

    public record UserData(
            Long id,
            String name,
            List<DataPointDto> dataPoint
    ) {}

    public record DataPointDto(
            String date,
            Double point
    ) {}
}
