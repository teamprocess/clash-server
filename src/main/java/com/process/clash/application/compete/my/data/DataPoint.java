package com.process.clash.application.compete.my.data;

import java.time.LocalDate;

public record DataPoint(
        LocalDate date,
        Double rate
) {
}
