package com.process.clash.application.record.v2.util;

import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class RecordSessionWindowCalculatorTest {

    private static final ZoneId UTC = ZoneId.of("UTC");

    // 윈도우: 2026-03-11 06:00 ~ 2026-03-12 06:00
    private static final LocalDateTime WINDOW_START = LocalDateTime.of(2026, 3, 11, 6, 0);
    private static final LocalDateTime WINDOW_END   = LocalDateTime.of(2026, 3, 12, 6, 0);

    private RecordSessionV2 session(LocalDateTime start, LocalDateTime end) {
        return new RecordSessionV2(
            1L, 1L,
            RecordSessionTypeV2.TASK,
            10L, "과목", 11L, "태스크", null,
            start.toInstant(ZoneOffset.UTC),
            end == null ? null : end.toInstant(ZoneOffset.UTC)
        );
    }

    @Nested
    @DisplayName("윈도우 내 완전히 포함된 세션")
    class SessionFullyInsideWindow {

        @Test
        @DisplayName("세션이 윈도우 안에 완전히 있으면 세션 길이를 반환한다")
        void fullyInsideWindow_returnsSessionDuration() {
            LocalDateTime start = LocalDateTime.of(2026, 3, 11, 9, 0);
            LocalDateTime end   = LocalDateTime.of(2026, 3, 11, 11, 0);

            long seconds = RecordSessionWindowCalculator.secondsInWindow(session(start, end), WINDOW_START, WINDOW_END, UTC);

            assertThat(seconds).isEqualTo(2 * 3600L); // 2시간
        }
    }

    @Nested
    @DisplayName("윈도우 경계를 벗어난 세션")
    class SessionCrossedBoundary {

        @Test
        @DisplayName("세션 시작이 윈도우 이전이면 윈도우 시작부터 계산한다")
        void sessionStartedBeforeWindow_cappedAtWindowStart() {
            LocalDateTime start = WINDOW_START.minusHours(2);
            LocalDateTime end   = LocalDateTime.of(2026, 3, 11, 8, 0);

            long seconds = RecordSessionWindowCalculator.secondsInWindow(session(start, end), WINDOW_START, WINDOW_END, UTC);

            // 06:00 ~ 08:00 = 2시간
            assertThat(seconds).isEqualTo(2 * 3600L);
        }

        @Test
        @DisplayName("세션 종료가 윈도우 이후이면 윈도우 끝까지만 계산한다")
        void sessionEndedAfterWindow_cappedAtWindowEnd() {
            LocalDateTime start = LocalDateTime.of(2026, 3, 11, 20, 0);
            LocalDateTime end   = WINDOW_END.plusHours(3);

            long seconds = RecordSessionWindowCalculator.secondsInWindow(session(start, end), WINDOW_START, WINDOW_END, UTC);

            // 20:00 ~ 다음날 06:00 = 10시간
            assertThat(seconds).isEqualTo(10 * 3600L);
        }

        @Test
        @DisplayName("세션이 윈도우 전체를 덮으면 24시간(86400초)을 반환한다")
        void sessionCoversEntireWindow_returns86400() {
            LocalDateTime start = WINDOW_START.minusHours(1);
            LocalDateTime end   = WINDOW_END.plusHours(1);

            long seconds = RecordSessionWindowCalculator.secondsInWindow(session(start, end), WINDOW_START, WINDOW_END, UTC);

            assertThat(seconds).isEqualTo(86_400L);
        }
    }

    @Nested
    @DisplayName("진행 중인 세션(endedAt == null)")
    class OngoingSession {

        @Test
        @DisplayName("진행 중인 세션은 windowEnd까지 계산한다")
        void ongoingSession_countedUntilWindowEnd() {
            LocalDateTime start = LocalDateTime.of(2026, 3, 11, 10, 0);

            long seconds = RecordSessionWindowCalculator.secondsInWindow(session(start, null), WINDOW_START, WINDOW_END, UTC);

            // 10:00 ~ 다음날 06:00 = 20시간
            assertThat(seconds).isEqualTo(20 * 3600L);
        }

        @Test
        @DisplayName("미래 날짜 윈도우에서 진행 중인 세션 — endLimit < windowStart이면 0을 반환한다")
        void ongoingSession_futureDateWindow_returnsZero() {
            // 현재 시각: 2026-03-11 10:00
            // 미래 날짜 윈도우: 2026-03-12 06:00 ~ 2026-03-13 06:00
            // endLimit = 현재 시각 = 2026-03-11 10:00 (dayStart 이전)
            LocalDateTime futureWindowStart = LocalDateTime.of(2026, 3, 12, 6, 0);
            LocalDateTime endLimit          = LocalDateTime.of(2026, 3, 11, 10, 0); // nowLocal

            // 오늘 시작된 진행 중인 세션
            LocalDateTime sessionStart = LocalDateTime.of(2026, 3, 11, 8, 0);

            long seconds = RecordSessionWindowCalculator.secondsInWindow(
                session(sessionStart, null), futureWindowStart, endLimit, UTC
            );

            // effectiveStart = max(sessionStart, futureWindowStart) = futureWindowStart
            // effectiveEnd   = endLimit = 2026-03-11 10:00
            // effectiveEnd < effectiveStart → 0
            assertThat(seconds).isEqualTo(0L);
        }
    }

    @Nested
    @DisplayName("윈도우 밖 세션")
    class SessionOutsideWindow {

        @Test
        @DisplayName("세션이 윈도우보다 이후에 끝났더라도 시작이 윈도우 이후면 0을 반환한다")
        void sessionStartedAfterWindow_returnsZero() {
            LocalDateTime start = WINDOW_END.plusHours(1);
            LocalDateTime end   = WINDOW_END.plusHours(3);

            long seconds = RecordSessionWindowCalculator.secondsInWindow(session(start, end), WINDOW_START, WINDOW_END, UTC);

            assertThat(seconds).isEqualTo(0L);
        }

        @Test
        @DisplayName("세션 종료가 윈도우 시작 이전이면 0을 반환한다")
        void sessionEndedBeforeWindow_returnsZero() {
            LocalDateTime start = WINDOW_START.minusHours(4);
            LocalDateTime end   = WINDOW_START.minusHours(1);

            long seconds = RecordSessionWindowCalculator.secondsInWindow(session(start, end), WINDOW_START, WINDOW_END, UTC);

            assertThat(seconds).isEqualTo(0L);
        }

        @Test
        @DisplayName("세션 시작과 종료가 같으면 0을 반환한다")
        void zeroLengthSession_returnsZero() {
            LocalDateTime point = LocalDateTime.of(2026, 3, 11, 12, 0);

            long seconds = RecordSessionWindowCalculator.secondsInWindow(session(point, point), WINDOW_START, WINDOW_END, UTC);

            assertThat(seconds).isEqualTo(0L);
        }
    }
}