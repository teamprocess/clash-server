package com.process.clash.infrastructure.config.github;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "github.oauth")
public record GithubOAuthProperties(
        String clientId,
        String clientSecret,
        String oauthBaseUrl,
        String apiBaseUrl
) {
}
