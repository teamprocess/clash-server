package com.process.clash.domain.user.user.entity;

import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;

import java.time.LocalDateTime;

public record User(
        Long  id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String username,
        String email,
        String name,
        String password,
        Role role,
        boolean ableToAddRival,
        String profileImage,
        boolean pomodoroEnabled,
        Integer pomodoroStudyMinute,
        Integer pomodoroBreakMinute,
        Major major,
        UserStatus userStatus
) {
    public static User createDefault(String username, String email, String name, String password) {
        return new User(
                null,
                null,
                null,
                username,
                email,
                name,
                password,
                Role.USER,
                true,
                "",
                false,   // 기본 공부 시간 (기본값이 없다면 null)
                25,
                5,
                Major.NONE,
                UserStatus.PENDING
        );

    }

    public User submitMajor(Major major) {
        // 모든 필드를 복사하되 major만 교체한 새 객체 반환
        return new User(
                this.id,
                this.createdAt,
                LocalDateTime.now(),
                this.username,
                this.email,
                this.name,
                this.password,
                this.role,
                this.ableToAddRival,
                this.profileImage,
                this.pomodoroEnabled,
                this.pomodoroStudyMinute,
                this.pomodoroBreakMinute,
                major, // 변경점
                this.userStatus
        );
    }

    public User withPomodoroSettings(Boolean pomodoroEnabled, Integer studyMinute, Integer breakMinute) {
        return new User(
                this.id,
                this.createdAt,
                LocalDateTime.now(),
                this.username,
                this.email,
                this.name,
                this.password,
                this.role,
                this.ableToAddRival,
                this.profileImage,
                pomodoroEnabled,
                studyMinute,
                breakMinute,
                this.major,
                this.userStatus
        );
    }

    public User active() {
        return new User(
                this.id,
                this.createdAt,
                LocalDateTime.now(),
                this.username,
                this.email,
                this.name,
                this.password,
                this.role,
                this.ableToAddRival,
                this.profileImage,
                this.pomodoroEnabled,
                this.pomodoroStudyMinute,
                this.pomodoroBreakMinute,
                this.major,
                UserStatus.ACTIVE
        );
    }

    public boolean isActive() {
        return UserStatus.ACTIVE.equals(this.userStatus);
    }

    public User updateSignupInfo(String username, String email, String name, String encodedPassword) {
        // 모든 필드를 그대로 가져오되, 회원가입 정보만 교체한 새 객체 생성
        return new User(
                this.id,
                this.createdAt,
                LocalDateTime.now(), // 수정 시간 갱신
                username,            // 변경
                email,               // 변경
                name,                // 변경
                encodedPassword,     // 변경
                this.role,
                this.ableToAddRival,
                this.profileImage,
                this.pomodoroEnabled,
                this.pomodoroStudyMinute,
                this.pomodoroBreakMinute,
                this.major,
                this.userStatus
        );
    }
}
