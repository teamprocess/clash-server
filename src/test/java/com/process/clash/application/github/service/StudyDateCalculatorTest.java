package com.process.clash.application.github.service;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.assertj.core.api.Assertions.assertThat;

class StudyDateCalculatorTest {

    @Test
    void toStudyDate_usesSixAmBoundary() {
        ZoneId kst = ZoneId.of("Asia/Seoul");
        StudyDateCalculator calculator = new StudyDateCalculator(kst, 6);

        Instant beforeBoundary = ZonedDateTime.of(LocalDate.of(2026, 1, 19), LocalTime.of(5, 59), kst)
                .toInstant();
        Instant atBoundary = ZonedDateTime.of(LocalDate.of(2026, 1, 19), LocalTime.of(6, 0), kst)
                .toInstant();

        assertThat(calculator.toStudyDate(beforeBoundary))
                .isEqualTo(LocalDate.of(2026, 1, 18));
        assertThat(calculator.toStudyDate(atBoundary))
                .isEqualTo(LocalDate.of(2026, 1, 19));
    }
}
