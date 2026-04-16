package com.process.clash.domain.user.userattendance.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class UserAttendanceTest {

    @Test
    @DisplayName("create는 isAttended=false인 출석 레코드를 생성한다")
    void create_createsNotAttendedRecord() {
        LocalDate date = LocalDate.of(2026, 4, 14);

        UserAttendance attendance = UserAttendance.create(1L, date);

        assertThat(attendance.id()).isNull();
        assertThat(attendance.createdAt()).isNull();
        assertThat(attendance.updatedAt()).isNull();
        assertThat(attendance.userId()).isEqualTo(1L);
        assertThat(attendance.attendanceDate()).isEqualTo(date);
        assertThat(attendance.isAttended()).isFalse();
    }

    @Test
    @DisplayName("markAttend는 동일한 id/userId/attendanceDate/createdAt을 유지하며 isAttended=true로 변경한다")
    void markAttend_returnsAttendedRecord_withSameIdentityFields() {
        LocalDate date = LocalDate.of(2026, 4, 14);
        Instant createdAt = Instant.now();
        UserAttendance attendance = new UserAttendance(1L, createdAt, Instant.now(), 2L, date, false);

        UserAttendance marked = attendance.markAttend();

        assertThat(marked.id()).isEqualTo(1L);
        assertThat(marked.userId()).isEqualTo(2L);
        assertThat(marked.attendanceDate()).isEqualTo(date);
        assertThat(marked.createdAt()).isEqualTo(createdAt);
        assertThat(marked.updatedAt()).isNull();
        assertThat(marked.isAttended()).isTrue();
    }

    @Test
    @DisplayName("이미 isAttended=true인 레코드에 markAttend를 호출해도 isAttended=true를 유지한다")
    void markAttend_remainsTrue_whenAlreadyAttended() {
        LocalDate date = LocalDate.of(2026, 4, 14);
        UserAttendance attendance = new UserAttendance(1L, Instant.now(), Instant.now(), 1L, date, true);

        UserAttendance marked = attendance.markAttend();

        assertThat(marked.isAttended()).isTrue();
    }

    @Test
    @DisplayName("currentAttendanceDate는 KST 오늘 또는 전날 날짜를 반환한다")
    void currentAttendanceDate_returnsKstTodayOrYesterday() {
        LocalDate kstToday = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate kstYesterday = kstToday.minusDays(1);

        LocalDate result = UserAttendance.currentAttendanceDate();

        assertThat(result).isIn(kstToday, kstYesterday);
    }

    @Test
    @DisplayName("currentAttendanceDate는 KST 06:00 이전이면 전날, 이후면 오늘 날짜를 반환한다")
    void currentAttendanceDate_shiftsByMinusHours6FromKst() {
        // KST 현재 시간에서 6시간을 뺀 날짜와 동일해야 한다
        LocalDate expected = java.time.ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
                .minusHours(6)
                .toLocalDate();

        LocalDate result = UserAttendance.currentAttendanceDate();

        assertThat(result).isEqualTo(expected);
    }
}