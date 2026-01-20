package com.process.clash.domain.user.userpomodorosetting.entity;

import java.time.LocalDateTime;

public record UserPomodoroSetting(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean pomodoroEnabled,
        int pomodoroStudyMinute,
        int pomodoroBreakMinute,
        Long userId
) {
}
