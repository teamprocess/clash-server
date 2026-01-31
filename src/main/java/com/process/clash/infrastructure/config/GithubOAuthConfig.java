package com.process.clash.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(GithubOAuthProperties.class)
public class GithubOAuthConfig {

    @Bean
    public WebClient githubOAuthWebClient(GithubOAuthProperties properties) {
        return WebClient.builder()
                .baseUrl(properties.oauthBaseUrl())
                .defaultHeader("Accept", "application/json")
                .defaultHeader("User-Agent", "clash-server")
                .build();
    }

    @Bean
    public WebClient githubApiWebClient(GithubOAuthProperties properties) {
        return WebClient.builder()
                .baseUrl(properties.apiBaseUrl())
                .defaultHeader("Accept", "application/vnd.github+json")
                .defaultHeader("User-Agent", "clash-server")
                .build();
    }
}
