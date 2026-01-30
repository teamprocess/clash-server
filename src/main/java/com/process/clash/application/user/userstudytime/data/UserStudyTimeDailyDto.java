package com.process.clash.application.user.userstudytime.data;

import java.time.LocalDate;

public record UserStudyTimeDailyDto(
        LocalDate date,
        long studyTimeSeconds
) {}
