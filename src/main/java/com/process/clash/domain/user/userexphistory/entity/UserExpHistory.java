package com.process.clash.domain.user.userexphistory.entity;

import com.process.clash.domain.user.userexphistory.enums.ExpActingCategory;

import java.time.LocalDate;
import java.time.Instant;

public record UserExpHistory(
        Long id,
        Instant createdAt,
        LocalDate date,
        int earnExp,
        ExpActingCategory actingCategory,
        Long userId
) {
}
