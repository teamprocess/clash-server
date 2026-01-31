package com.process.clash.application.record.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public final class RecordDateCalculator {

    private RecordDateCalculator() {
    }

    public static LocalDate recordDate(ZonedDateTime now, int boundaryHour) {
        LocalDate recordDate = now.toLocalDate();
        if (now.getHour() < boundaryHour) {
            recordDate = recordDate.minusDays(1);
        }
        return recordDate;
    }

    public static LocalDateTime startOfRecordDay(ZonedDateTime now, int boundaryHour) {
        return recordDate(now, boundaryHour).atTime(boundaryHour, 0);
    }
}
