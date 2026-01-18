package com.process.clash.adapter.persistence.user.usergithub;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.user.usergithub.port.out.UserGitHubRepositoryPort;
import com.process.clash.domain.user.usergithub.entity.UserGitHub;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserGitHubPersistenceAdapter implements UserGitHubRepositoryPort {

    private final UserGitHubJpaMapper userGitHubJpaMapper;
    private final UserJpaRepository userJpaRepository;
    private final UserGitHubJpaRepository userGitHubJpaRepository;

    @Override
    public UserGitHub save(UserGitHub userGitHub) {

        UserJpaEntity userJpaEntity = userJpaRepository.getReferenceById(userGitHub.id());
        UserGitHubJpaEntity savedEntity =
                userGitHubJpaRepository.save(userGitHubJpaMapper.toJpaEntity(userGitHub, userJpaEntity));
        return userGitHubJpaMapper.toDomain(savedEntity);
    }

    @Override
    public List<UserGitHub> findByIdNotIn(List<Long> ids) {

        List<UserGitHubJpaEntity> userGitHubJpaEntities = userGitHubJpaRepository.findByIdNotIn(ids);

        return userGitHubJpaEntities.stream()
                .map(userGitHubJpaMapper::toDomain)
                .toList();
    }
}
