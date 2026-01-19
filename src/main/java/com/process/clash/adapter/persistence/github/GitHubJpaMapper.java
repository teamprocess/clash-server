package com.process.clash.adapter.persistence.github;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.github.entity.GitHub;
import org.springframework.stereotype.Component;

@Component
public class GitHubJpaMapper {

    public GitHubJpaEntity toJpaEntity(GitHub gitHub, UserJpaEntity userJpaEntity) {

        return new GitHubJpaEntity(
                gitHub.id(),
                gitHub.createdAt(),
                gitHub.updatedAt(),
                gitHub.date(),
                gitHub.contributionCount(),
                userJpaEntity
        );
    }

    public GitHub toDomain(GitHubJpaEntity gitHubJpaEntity) {

        return new GitHub(
                gitHubJpaEntity.getId(),
                gitHubJpaEntity.getCreatedAt(),
                gitHubJpaEntity.getUpdatedAt(),
                gitHubJpaEntity.getDate(),
                gitHubJpaEntity.getContributionCount(),
                gitHubJpaEntity.getUser().getId()
        );
    }
}
