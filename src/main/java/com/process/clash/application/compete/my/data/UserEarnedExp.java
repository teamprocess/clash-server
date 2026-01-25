package com.process.clash.application.compete.my.data;

import java.time.LocalDate;

public record UserEarnedExp(
        LocalDate date,
        Double avgEarnedExp
) {
}
