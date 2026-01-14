package com.process.clash.domain.common.enums;

import com.process.clash.application.user.user.exception.exception.badrequest.InvalidWeekCategoryException;

import java.util.Arrays;

public enum WeekCategory {
    SUN,
    MON,
    TUE,
    WED,
    THU,
    FRI,
    SAT;

    public static WeekCategory from(String value) {
        if (value == null) {
            throw new InvalidWeekCategoryException();
        }

        return Arrays.stream(values())
                .filter(v -> v.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(InvalidWeekCategoryException::new);
    }
}
