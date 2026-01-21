package com.process.clash.infrastructure.config;

import java.time.ZoneId;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RecordProperties.class)
public class RecordConfig {

    @Bean
    public ZoneId recordZoneId(RecordProperties properties) {
        return ZoneId.of(properties.timezone());
    }
}
