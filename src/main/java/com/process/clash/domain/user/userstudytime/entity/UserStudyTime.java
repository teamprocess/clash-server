package com.process.clash.domain.user.userstudytime.entity;

import java.time.LocalDate;

public record UserStudyTime(
        Long id,
        LocalDate date,
        long totalStudyTimeSeconds,
        Long userId
) {}
