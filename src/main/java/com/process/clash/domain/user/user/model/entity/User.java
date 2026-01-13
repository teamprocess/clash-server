package com.process.clash.domain.user.user.model.entity;

import com.process.clash.domain.common.enums.Major;

import java.time.LocalDateTime;

public record User(
        Long  id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String username,
        String name,
        String password,
        Boolean ableToAddRival,
        String profileImage,
        Boolean pomodoroEnabled,
        Integer pomodoroStudyMinute,
        Integer pomodoroBreakMinute,
        Major major
) {
}
