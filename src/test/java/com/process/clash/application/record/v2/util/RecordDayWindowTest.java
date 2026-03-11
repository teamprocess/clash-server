package com.process.clash.application.record.v2.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class RecordDayWindowTest {

    private static final ZoneId UTC = ZoneId.of("UTC");
    private static final int BOUNDARY_HOUR = 6;

    // 고정 시각: 2026-03-11 10:00 UTC (경계 6시 이후이므로 기록일 = 2026-03-11)
    private static final Instant NOW = Instant.parse("2026-03-11T10:00:00Z");
    private static final Clock FIXED_CLOCK = Clock.fixed(NOW, UTC);

    @Nested
    @DisplayName("today() 팩토리")
    class TodayFactory {

        @Test
        @DisplayName("오늘 기록일 날짜가 recordDate로 설정된다")
        void today_recordDateIsCurrentRecordDay() {
            RecordDayWindow window = RecordDayWindow.today(UTC, BOUNDARY_HOUR, FIXED_CLOCK);

            assertThat(window.recordDate()).isEqualTo(LocalDate.of(2026, 3, 11));
            assertThat(window.todayRecordDate()).isTrue();
        }

        @Test
        @DisplayName("dayStart는 경계 시각(06:00)이다")
        void today_dayStartIsBoundaryHour() {
            RecordDayWindow window = RecordDayWindow.today(UTC, BOUNDARY_HOUR, FIXED_CLOCK);

            assertThat(window.dayStart()).isEqualTo(LocalDateTime.of(2026, 3, 11, 6, 0));
        }

        @Test
        @DisplayName("dayEnd는 dayStart + 24시간이다")
        void today_dayEndIsDayStartPlusOneDay() {
            RecordDayWindow window = RecordDayWindow.today(UTC, BOUNDARY_HOUR, FIXED_CLOCK);

            assertThat(window.dayEnd()).isEqualTo(window.dayStart().plusDays(1));
        }

        @Test
        @DisplayName("오늘의 endLimit은 현재 시각이다")
        void today_endLimitIsCurrentTime() {
            RecordDayWindow window = RecordDayWindow.today(UTC, BOUNDARY_HOUR, FIXED_CLOCK);

            // NOW = 2026-03-11T10:00 < dayEnd = 2026-03-12T06:00 → endLimit = nowLocal
            assertThat(window.endLimit()).isEqualTo(LocalDateTime.of(2026, 3, 11, 10, 0));
        }

        @Test
        @DisplayName("경계 시각 이전에 조회하면 기록일이 전날로 계산된다")
        void today_beforeBoundaryHour_recordDateIsYesterday() {
            // 2026-03-11 04:00 UTC → 경계(06:00) 이전이므로 기록일 = 2026-03-10
            Instant beforeBoundary = Instant.parse("2026-03-11T04:00:00Z");
            Clock clock = Clock.fixed(beforeBoundary, UTC);

            RecordDayWindow window = RecordDayWindow.today(UTC, BOUNDARY_HOUR, clock);

            assertThat(window.recordDate()).isEqualTo(LocalDate.of(2026, 3, 10));
            assertThat(window.dayStart()).isEqualTo(LocalDateTime.of(2026, 3, 10, 6, 0));
            assertThat(window.dayEnd()).isEqualTo(LocalDateTime.of(2026, 3, 11, 6, 0));
            assertThat(window.endLimit()).isEqualTo(LocalDateTime.of(2026, 3, 11, 4, 0));
        }
    }

    @Nested
    @DisplayName("of() 팩토리 - 과거 날짜")
    class OfPastDate {

        // 현재 시각 기준 과거 날짜 = 2026-03-10
        private static final LocalDate PAST_DATE = LocalDate.of(2026, 3, 10);

        @Test
        @DisplayName("과거 날짜는 todayRecordDate가 false다")
        void of_pastDate_isNotTodayRecordDate() {
            RecordDayWindow window = RecordDayWindow.of(PAST_DATE, UTC, BOUNDARY_HOUR, FIXED_CLOCK);

            assertThat(window.todayRecordDate()).isFalse();
        }

        @Test
        @DisplayName("과거 날짜의 endLimit은 dayEnd(하루 전체)이다")
        void of_pastDate_endLimitIsDayEnd() {
            RecordDayWindow window = RecordDayWindow.of(PAST_DATE, UTC, BOUNDARY_HOUR, FIXED_CLOCK);

            // nowLocal(2026-03-11T10:00) > dayEnd(2026-03-11T06:00) → endLimit = dayEnd
            assertThat(window.dayStart()).isEqualTo(LocalDateTime.of(2026, 3, 10, 6, 0));
            assertThat(window.dayEnd()).isEqualTo(LocalDateTime.of(2026, 3, 11, 6, 0));
            assertThat(window.endLimit()).isEqualTo(window.dayEnd());
        }
    }

    @Nested
    @DisplayName("of() 팩토리 - 미래 날짜")
    class OfFutureDate {

        // 현재 시각 기준 미래 날짜 = 2026-03-12
        private static final LocalDate FUTURE_DATE = LocalDate.of(2026, 3, 12);

        @Test
        @DisplayName("미래 날짜는 todayRecordDate가 false다")
        void of_futureDate_isNotTodayRecordDate() {
            RecordDayWindow window = RecordDayWindow.of(FUTURE_DATE, UTC, BOUNDARY_HOUR, FIXED_CLOCK);

            assertThat(window.todayRecordDate()).isFalse();
        }

        @Test
        @DisplayName("미래 날짜의 endLimit은 현재 시각(dayStart 이전)이다")
        void of_futureDate_endLimitIsCurrentTimeBeforeDayStart() {
            RecordDayWindow window = RecordDayWindow.of(FUTURE_DATE, UTC, BOUNDARY_HOUR, FIXED_CLOCK);

            // nowLocal(2026-03-11T10:00) < dayEnd(2026-03-13T06:00) → endLimit = nowLocal
            assertThat(window.dayStart()).isEqualTo(LocalDateTime.of(2026, 3, 12, 6, 0));
            assertThat(window.endLimit()).isEqualTo(LocalDateTime.of(2026, 3, 11, 10, 0));
            // 핵심: endLimit < dayStart → 미래 날짜 판별 가능
            assertThat(window.endLimit()).isBefore(window.dayStart());
        }

        @Test
        @DisplayName("미래 날짜의 학습 가능 구간이 0임을 endLimit < dayStart로 확인한다")
        void of_futureDate_effectiveWindowIsEmpty() {
            RecordDayWindow window = RecordDayWindow.of(FUTURE_DATE, UTC, BOUNDARY_HOUR, FIXED_CLOCK);

            // endLimit < dayStart → 유효 구간 없음(학습 시간 = 0)
            assertThat(window.endLimit()).isBefore(window.dayStart());
        }
    }

    @Nested
    @DisplayName("of() 팩토리 - 오늘 날짜")
    class OfTodayDate {

        // 현재 시각 기준 오늘 날짜 = 2026-03-11
        private static final LocalDate TODAY = LocalDate.of(2026, 3, 11);

        @Test
        @DisplayName("오늘 날짜는 todayRecordDate가 true다")
        void of_todayDate_isTodayRecordDate() {
            RecordDayWindow window = RecordDayWindow.of(TODAY, UTC, BOUNDARY_HOUR, FIXED_CLOCK);

            assertThat(window.todayRecordDate()).isTrue();
        }

        @Test
        @DisplayName("오늘 날짜의 endLimit은 현재 시각이다")
        void of_todayDate_endLimitIsCurrentTime() {
            RecordDayWindow window = RecordDayWindow.of(TODAY, UTC, BOUNDARY_HOUR, FIXED_CLOCK);

            assertThat(window.endLimit()).isEqualTo(LocalDateTime.of(2026, 3, 11, 10, 0));
        }
    }
}