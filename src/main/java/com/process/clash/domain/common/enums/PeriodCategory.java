package com.process.clash.domain.common.enums;

import com.process.clash.application.user.user.exception.exception.badrequest.InvalidPeriodCategoryException;

import java.util.Arrays;

public enum PeriodCategory {
    DAY,
    WEEK,
    MONTH,
    SEASON, // 아직 필요 없지만 그냥 만들어둠
    YEAR; // 아직 필요 없지만 그냥 만들어둠

    public static PeriodCategory from(String value) {
        if (value == null) {
            throw new InvalidPeriodCategoryException();
        }

        return Arrays.stream(values())
                .filter(v -> v.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(InvalidPeriodCategoryException::new);
    }
}
