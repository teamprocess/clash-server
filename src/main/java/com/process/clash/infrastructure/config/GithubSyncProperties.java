package com.process.clash.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "github.sync")
public record GithubSyncProperties(
        String apiBaseUrl,
        String defaultToken,
        int recomputeDays,
        String timezone,
        int dayBoundaryHour,
        int maxConcurrency
) {
}
