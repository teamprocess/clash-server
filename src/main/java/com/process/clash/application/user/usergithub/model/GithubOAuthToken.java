package com.process.clash.application.user.usergithub.model;

public record GithubOAuthToken(
        String accessToken,
        String tokenType,
        String scope
) {
}
