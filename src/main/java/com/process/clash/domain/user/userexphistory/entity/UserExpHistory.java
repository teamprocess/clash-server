package com.process.clash.domain.user.userexphistory.entity;

import com.process.clash.domain.user.userexphistory.enums.ExpActingCategory;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserExpHistory(
        Long id,
        LocalDateTime createdAt,
        LocalDate date,
        int earnExp,
        ExpActingCategory actingCategory,
        Long userId
) {
}
