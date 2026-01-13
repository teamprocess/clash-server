package com.process.clash.domain.user.model.entity;

import com.process.clash.domain.common.enums.Major;

import java.time.LocalDateTime;

public record User(
        Long  id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String username,
        String name,
        String password,
        boolean ableToAddRival,
        Major major
) {
}
