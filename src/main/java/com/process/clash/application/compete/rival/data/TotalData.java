package com.process.clash.application.compete.rival.data;

import java.time.LocalDate;
import java.util.List;

public record TotalData(
        Long id,
        String name,
        List<DataPoint> dataPoint
) {
    public record DataPoint(
            LocalDate date,
            Double point
    ) {}

}