package com.process.clash.domain.common.enums;

import com.process.clash.application.user.user.exception.exception.badrequest.InvalidTargetCategoryException;

import java.util.Arrays;

public enum TargetCategory {
    GITHUB,
    SOLVED_AC,
    ACTIVE_TIME,
    EXP;

    public static TargetCategory from(String value) {
        if (value == null) {
            throw new InvalidTargetCategoryException();
        }

        return Arrays.stream(values())
                .filter(v -> v.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(InvalidTargetCategoryException::new);
    }
}
