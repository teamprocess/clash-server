package com.process.clash.application.compete.my.data;

import java.time.LocalDate;

public record Streak(
        LocalDate date,
        Integer detailedInfo
) {
}
