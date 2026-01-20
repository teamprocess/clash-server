package com.process.clash.adapter.web.compete.rival.dto;

import java.time.LocalDate;
import java.util.List;

public class CompareWithRivalsDto {

    public record Response(
            String category,
            String period,
            List<TotalData> totalData
    ) {}

    private record TotalData(
            Long id,
            String name,
            List<DataPoint> dataPoint
    ) {}

    private record DataPoint(
            LocalDate date,
            Double point
    ) {}
}

