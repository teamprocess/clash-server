package com.process.clash.application.record.util;

import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public final class RecordSessionWindowCalculator {

    private RecordSessionWindowCalculator() {
    }

    public static long secondsInWindow(
        RecordSessionV2 session,
        LocalDateTime windowStart,
        LocalDateTime windowEnd,
        ZoneId zoneId
    ) {
        LocalDateTime sessionStart = LocalDateTime.ofInstant(session.startedAt(), zoneId);
        LocalDateTime effectiveStart = sessionStart.isAfter(windowStart) ? sessionStart : windowStart;
        LocalDateTime sessionEnd = session.endedAt() == null ? null : LocalDateTime.ofInstant(session.endedAt(), zoneId);
        LocalDateTime effectiveEnd = sessionEnd == null ? windowEnd : sessionEnd;

        if (effectiveEnd.isAfter(windowEnd)) {
            effectiveEnd = windowEnd;
        }
        if (!effectiveEnd.isAfter(effectiveStart)) {
            return 0L;
        }
        return ChronoUnit.SECONDS.between(effectiveStart, effectiveEnd);
    }
}
