package com.process.clash.adapter.persistence.githubinfo;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.githubinfo.entity.GitHubInfo;
import org.springframework.stereotype.Component;

@Component
public class GitHubInfoJpaMapper {

    public GitHubInfoJpaEntity toJpaEntity(GitHubInfo gitHubInfo, UserJpaEntity userJpaEntity) {

        return new GitHubInfoJpaEntity(
                gitHubInfo.id(),
                gitHubInfo.createdAt(),
                gitHubInfo.updatedAt(),
                gitHubInfo.date(),
                gitHubInfo.contributionCount(),
                userJpaEntity
        );
    }

    public GitHubInfo toDomain(GitHubInfoJpaEntity gitHubInfoJpaEntity) {

        return new GitHubInfo(
                gitHubInfoJpaEntity.getId(),
                gitHubInfoJpaEntity.getCreatedAt(),
                gitHubInfoJpaEntity.getUpdatedAt(),
                gitHubInfoJpaEntity.getDate(),
                gitHubInfoJpaEntity.getContributionCount(),
                gitHubInfoJpaEntity.getUser().getId()
        );
    }
}
