package com.process.clash.domain.user.usercookiehistory.entity;

import com.process.clash.domain.common.enums.ActingCategory;

import java.time.LocalDateTime;

public record UserCookieHistory(
        Long id,
        LocalDateTime createdAt,
        ActingCategory actingCategory,
        int variation,
        Long productId,
        Long userId
) {}
