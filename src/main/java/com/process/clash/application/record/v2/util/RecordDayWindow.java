package com.process.clash.application.record.v2.util;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public record RecordDayWindow(
    LocalDate recordDate,
    LocalDateTime dayStart,
    LocalDateTime dayEnd,
    LocalDateTime endLimit,
    boolean todayRecordDate
) {

    public static RecordDayWindow today(ZoneId zoneId, int boundaryHour) {
        return today(zoneId, boundaryHour, Clock.system(zoneId));
    }

    public static RecordDayWindow today(ZoneId zoneId, int boundaryHour, Clock clock) {
        ZonedDateTime now = ZonedDateTime.now(clock.withZone(zoneId));
        LocalDate todayRecordDate = RecordDateCalculator.recordDate(now, boundaryHour);
        return from(now, todayRecordDate, boundaryHour);
    }

    public static RecordDayWindow of(LocalDate recordDate, ZoneId zoneId, int boundaryHour) {
        return from(ZonedDateTime.now(zoneId), recordDate, boundaryHour);
    }

    private static RecordDayWindow from(ZonedDateTime now, LocalDate recordDate, int boundaryHour) {
        LocalDate todayRecordDate = RecordDateCalculator.recordDate(now, boundaryHour);
        LocalDateTime dayStart = recordDate.atTime(boundaryHour, 0);
        LocalDateTime dayEnd = dayStart.plusDays(1);
        LocalDateTime nowLocal = now.toLocalDateTime();
        boolean isTodayRecordDate = recordDate.equals(todayRecordDate);
        LocalDateTime endLimit = nowLocal.isBefore(dayEnd) ? nowLocal : dayEnd;
        return new RecordDayWindow(recordDate, dayStart, dayEnd, endLimit, isTodayRecordDate);
    }
}