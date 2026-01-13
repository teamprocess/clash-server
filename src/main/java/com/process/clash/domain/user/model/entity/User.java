package com.process.clash.domain.user.model.entity;

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
                true,
                null,
                false,
                25,   // 기본 공부 시간 (기본값이 없다면 null)
                5,
                Major.NONE
        );
    }
}
