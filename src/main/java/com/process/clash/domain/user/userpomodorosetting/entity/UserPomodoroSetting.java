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

    public static UserPomodoroSetting createDefault(Long userId) {

        return new UserPomodoroSetting(
                null,
                null,
                null,
                false,
                25,
                5,
                userId
        );
    }
}
