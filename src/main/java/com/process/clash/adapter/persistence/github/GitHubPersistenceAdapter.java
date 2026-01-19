package com.process.clash.adapter.persistence.github;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.github.port.out.GitHubRepositoryPort;
import com.process.clash.domain.github.entity.GitHub;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GitHubPersistenceAdapter implements GitHubRepositoryPort {

    private final GitHubJpaMapper gitHubJpaMapper;
    private final GitHubJpaRepository gitHubJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public GitHub save(GitHub gitHub) {

        UserJpaEntity userJpaEntity = userJpaRepository.getReferenceById(gitHub.userId());
        GitHubJpaEntity savedEntity = gitHubJpaRepository.save(gitHubJpaMapper.toJpaEntity(gitHub, userJpaEntity));
        return gitHubJpaMapper.toDomain(savedEntity);
    }
}
