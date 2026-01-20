package com.process.clash.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "record")
public record RecordProperties(
    String timezone,
    int dayBoundaryHour
) {
}
