package com.process.clash.application.github.model;

import java.util.List;

public record GithubSyncTarget(
        Long userId,
        String githubLogin,
        String githubUserNodeId,
        List<String> emails,
        String accessToken
) {
}
