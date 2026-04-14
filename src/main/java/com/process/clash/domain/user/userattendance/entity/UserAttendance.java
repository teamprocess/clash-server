package com.process.clash.domain.user.userattendance.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public record UserAttendance(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        Long userId,
        LocalDate attendanceDate,
        boolean isAttended
) {
    public static UserAttendance create(Long userId, LocalDate attendanceDate) {
        return new UserAttendance(
                null,
                null,
                null,
                userId,
                attendanceDate,
                false
        );
    }

    public UserAttendance markAttend() {
        return new UserAttendance(
                this.id,
                this.createdAt,
                null,
                this.userId,
                this.attendanceDate,
                true
        );
    }

    /**
     * KST 06:00을 자정으로 하는 출석 날짜를 반환합니다.
     * - 06:00 이후 → 오늘 날짜
     * - 06:00 미만 → 전날 날짜
     */
    public static LocalDate currentAttendanceDate() {
        ZonedDateTime kstNow = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        return kstNow.minusHours(6).toLocalDate();
    }
}