package com.process.clash.adapter.persistence.github;

import com.process.clash.adapter.persistence.user.usergithub.UserGitHubJpaEntity;
import com.process.clash.adapter.persistence.user.usergithub.UserGitHubJpaRepository;
import com.process.clash.application.github.model.GithubSyncTarget;
import com.process.clash.application.github.port.out.GithubSyncTargetPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GitHubSyncTargetJpaAdapter implements GithubSyncTargetPort {

    private final UserGitHubJpaRepository userGitHubJpaRepository;

    @Override
    public List<GithubSyncTarget> findSyncTargets() {
        List<UserGitHubJpaEntity> entities = userGitHubJpaRepository.findAllWithUserAndGitHubId();
        return entities.stream()
                .map(entity -> new GithubSyncTarget(
                        entity.getUser().getId(),
                        entity.getGitHubId(),
                        entity.getGithubUserNodeId(),
                        parseEmails(entity.getGithubEmails()),
                        entity.getGithubAccessToken()
                ))
                .toList();
    }

    @Override
    public Optional<GithubSyncTarget> findSyncTargetByUserId(Long userId) {
        return userGitHubJpaRepository.findByUserIdWithUser(userId)
                .map(entity -> new GithubSyncTarget(
                        entity.getUser().getId(),
                        entity.getGitHubId(),
                        entity.getGithubUserNodeId(),
                        parseEmails(entity.getGithubEmails()),
                        entity.getGithubAccessToken()
                ));
    }

    private List<String> parseEmails(String raw) {
        if (raw == null || raw.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .collect(Collectors.toList());
    }
}
