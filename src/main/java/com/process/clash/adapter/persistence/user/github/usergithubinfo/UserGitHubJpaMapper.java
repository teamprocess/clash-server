package com.process.clash.adapter.persistence.user.github.usergithubinfo;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.user.github.usergithubinfo.entity.UserGitHubInfo;
import org.springframework.stereotype.Component;

@Component
public class UserGitHubJpaMapper {

    public UserGitHubInfoJpaEntity toJpaEntity(UserGitHubInfo userGitHubInfo, UserJpaEntity userJpaEntity) {

        return new UserGitHubInfoJpaEntity(
                userGitHubInfo.id(),
                userGitHubInfo.createdAt(),
                userGitHubInfo.updatedAt(),
                userGitHubInfo.date(),
                userGitHubInfo.contributionCount(),
                userJpaEntity
        );
    }

    public UserGitHubInfo toDomain(UserGitHubInfoJpaEntity userGitHubInfoJpaEntity) {

        return new UserGitHubInfo(
                userGitHubInfoJpaEntity.getId(),
                userGitHubInfoJpaEntity.getCreatedAt(),
                userGitHubInfoJpaEntity.getUpdatedAt(),
                userGitHubInfoJpaEntity.getDate(),
                userGitHubInfoJpaEntity.getContributionCount(),
                userGitHubInfoJpaEntity.getUser().getId()
        );
    }
}
