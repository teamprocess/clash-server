package com.process.clash.application.record.util;

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
        // 집계 구간 시작 이전에 시작한 세션은 집계 시작 시각부터 계산한다.
        LocalDateTime effectiveStart = sessionStart.isAfter(windowStart) ? sessionStart : windowStart;
        LocalDateTime sessionEnd = session.endedAt() == null ? null : LocalDateTime.ofInstant(session.endedAt(), zoneId);
        // 종료되지 않은 세션은 집계 구간 끝(windowEnd)까지 진행된 것으로 간주한다.
        LocalDateTime effectiveEnd = sessionEnd == null ? windowEnd : sessionEnd;

        // 세션 종료가 집계 구간을 넘어가면 구간 끝으로 잘라서 계산한다.
        if (effectiveEnd.isAfter(windowEnd)) {
            effectiveEnd = windowEnd;
        }
        // 유효 구간이 없으면(종료 <= 시작) 학습 시간은 0초다.
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
