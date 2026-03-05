package com.process.clash.infrastructure.config.github;

import com.process.clash.application.github.service.StudyDateCalculator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Clock;
import java.time.ZoneId;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(GithubSyncProperties.class)
public class GithubSyncConfig {

    @Bean
    public WebClient githubWebClient(GithubSyncProperties properties) {
        return WebClient.builder()
                .baseUrl(properties.apiBaseUrl())
                .defaultHeader("Accept", "application/json")
                .defaultHeader("User-Agent", "clash-server")
                .build();
    }

    @Bean
    public StudyDateCalculator studyDateCalculator(GithubSyncProperties properties) {
        ZoneId zoneId = ZoneId.of(properties.timezone());
        return new StudyDateCalculator(zoneId, properties.dayBoundaryHour());
    }

    @Bean
    public Clock clock(@Value("${github.sync.timezone:Asia/Seoul}") String timezone) {
        return Clock.system(ZoneId.of(timezone));
    }

    @Bean(destroyMethod = "shutdown")
    public ExecutorService githubSyncExecutor(GithubSyncProperties properties) {
        // 동기화 병렬 처리 시 사용할 제한된 스레드풀
        int maxConcurrency = Math.max(1, properties.maxConcurrency());
        return Executors.newFixedThreadPool(maxConcurrency);
    }
}
