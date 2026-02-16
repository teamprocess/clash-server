package com.process.clash.domain.user.userpomodorosetting.entity;

import java.time.Instant;

public record UserPomodoroSetting(
        Long id,
        Instant createdAt,
        Instant updatedAt,
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

    public UserPomodoroSetting updatePomodoroSetting(
            boolean pomodoroEnabled,
            int pomodoroStudyMinute,
            int pomodoroBreakMinute
    ) {

        return new UserPomodoroSetting(
                this.id,
                this.createdAt,
                null,
                pomodoroEnabled,
                pomodoroStudyMinute,
                pomodoroBreakMinute,
                this.userId
        );
    }
}
