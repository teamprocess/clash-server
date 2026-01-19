package com.process.clash.adapter.persistence.user.github.usergithub;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.user.usergithub.entity.UserGitHub;
import org.springframework.stereotype.Component;

@Component
public class UserGitHubJpaMapper {

    public UserGitHubJpaEntity toJpaEntity(UserGitHub userGitHub, UserJpaEntity userJpaEntity) {
        return new UserGitHubJpaEntity(
                userGitHub.id(),
                userGitHub.gitHubId(),
                userJpaEntity
        );
    }

    public UserGitHub toDomain(UserGitHubJpaEntity userGitHubJpaEntity) {
        return new UserGitHub(
                userGitHubJpaEntity.getId(),
                userGitHubJpaEntity.getGitHubId(),
                userGitHubJpaEntity.getUser().getId()
        );
    }
}
