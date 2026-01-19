package com.process.clash.application.github.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudyDateCalculator {

    private final ZoneId zoneId;
    private final int dayBoundaryHour;

    public StudyDateCalculator(ZoneId zoneId, int dayBoundaryHour) {
        this.zoneId = Objects.requireNonNull(zoneId, "zoneId");
        this.dayBoundaryHour = dayBoundaryHour;
    }

    public LocalDate toStudyDate(Instant instant) {
        return instant.atZone(zoneId)
                .minusHours(dayBoundaryHour)
                .toLocalDate();
    }

    public Instant rangeStartUtc(LocalDate studyDate) {
        ZonedDateTime start = studyDate.atTime(dayBoundaryHour, 0).atZone(zoneId);
        return start.toInstant();
    }

    public Instant rangeEndExclusiveUtc(LocalDate studyDate) {
        ZonedDateTime end = studyDate.plusDays(1).atTime(dayBoundaryHour, 0).atZone(zoneId);
        return end.toInstant();
    }

    public List<LocalDate> recentStudyDates(Instant now, int days) {
        if (days <= 0) {
            return List.of();
        }
        LocalDate end = toStudyDate(now);
        LocalDate start = end.minusDays(days - 1L);
        List<LocalDate> dates = new ArrayList<>(days);
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            dates.add(date);
        }
        return dates;
    }
}
