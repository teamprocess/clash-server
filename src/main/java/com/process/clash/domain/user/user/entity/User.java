package com.process.clash.domain.user.user.entity;

import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.common.enums.Role;

import java.time.LocalDateTime;

public record User(
        Long  id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String username,
        String name,
        String password,
        Role role,
        Boolean ableToAddRival,
        String profileImage,
        Boolean pomodoroEnabled,
        Integer pomodoroStudyMinute,
        Integer pomodoroBreakMinute,
        Major major
) {
    public static User createDefault(String username, String name, String password) {
        return new User(
                null,
                null,
                null,
                username,
                name,
                password,
                Role.USER,
                null,
                "",
                false,   // 기본 공부 시간 (기본값이 없다면 null)
                25,
                5,
                Major.NONE
        );
    }

    public User withPomodoroSettings(Boolean pomodoroEnabled, Integer studyMinute, Integer breakMinute) {
        return new User(
                this.id,
                this.createdAt,
                LocalDateTime.now(),
                this.username,
                this.name,
                this.password,
                this.role,
                this.ableToAddRival,
                this.profileImage,
                pomodoroEnabled,
                studyMinute,
                breakMinute,
                this.major
        );
    }
}
