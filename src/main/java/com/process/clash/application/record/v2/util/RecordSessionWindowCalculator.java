package com.process.clash.application.record.v2.util;

import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public static Map<Long, Long> taskStudyTimesInWindow(
        List<RecordSessionV2> sessions,
        LocalDateTime windowStart,
        LocalDateTime windowEnd,
        ZoneId zoneId
    ) {
        return sessions.stream()
            .filter(session -> session.sessionType() == RecordSessionTypeV2.TASK)
            .filter(session -> session.taskId() != null)
            .collect(Collectors.groupingBy(
                RecordSessionV2::taskId,
                Collectors.summingLong(session -> secondsInWindow(
                    session,
                    windowStart,
                    windowEnd,
                    zoneId
                ))
            ));
    }
}