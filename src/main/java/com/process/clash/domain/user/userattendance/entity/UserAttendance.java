package com.process.clash.domain.user.userattendance.entity;

import java.time.Instant;

public record UserAttendance(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        Long userId,
        boolean isAttended
) {
    public static UserAttendance create(Long userId) {
        return new UserAttendance(
                null,
                null,
                null,
                userId,
                false
        );
    }

    public UserAttendance markAttend(Long id) {
        return new UserAttendance(
                this.id,
                this.createdAt,
                null,
                this.userId,
                true
        );
    }
}