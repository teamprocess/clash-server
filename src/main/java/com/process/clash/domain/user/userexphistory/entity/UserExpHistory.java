package com.process.clash.domain.user.userexphistory.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserExpHistory(
        Long id,
        LocalDateTime createdAt,
        LocalDate date,
        int earnExp,
        Long userId
) {
}
