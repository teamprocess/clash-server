package com.process.clash.adapter.persistence.githubinfo;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.githubinfo.port.out.GitHubInfoRepositoryPort;
import com.process.clash.domain.githubinfo.entity.GitHubInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GitHubInfoPersistenceAdapter implements GitHubInfoRepositoryPort {

    private final GitHubInfoJpaMapper gitHubInfoJpaMapper;
    private final GitHubInfoJpaRepository gitHubInfoJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public GitHubInfo save(GitHubInfo gitHubInfo) {

        UserJpaEntity userJpaEntity = userJpaRepository.getReferenceById(gitHubInfo.userId());
        GitHubInfoJpaEntity savedEntity = gitHubInfoJpaRepository.save(gitHubInfoJpaMapper.toJpaEntity(gitHubInfo, userJpaEntity));
        return gitHubInfoJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<GitHubInfo> findByUserIdAndDate(Long userId, LocalDate date) {

        return gitHubInfoJpaRepository.findByUserIdAndDate(userId, date)
                .map(gitHubInfoJpaMapper::toDomain);
    }
}
