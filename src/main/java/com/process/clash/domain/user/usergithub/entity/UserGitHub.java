package com.process.clash.domain.user.usergithub.entity;

public record UserGitHub(
        Long id,
        String gitHubId,
        Long userId,
        String githubUserNodeId,
        String githubEmails,
        String githubAccessToken
) {
}
