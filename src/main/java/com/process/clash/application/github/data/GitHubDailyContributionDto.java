package com.process.clash.application.github.data;

import java.time.LocalDate;

public record GitHubDailyContributionDto(
        LocalDate date,
        int count
) {}
