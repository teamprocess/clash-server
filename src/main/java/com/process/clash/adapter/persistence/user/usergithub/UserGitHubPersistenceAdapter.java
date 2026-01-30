package com.process.clash.adapter.persistence.user.usergithub;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.compete.rival.rival.data.AbleRivalInfoForRival;
import com.process.clash.application.user.usergithub.port.out.UserGitHubRepositoryPort;
import com.process.clash.domain.user.usergithub.entity.UserGitHub;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserGitHubPersistenceAdapter implements UserGitHubRepositoryPort {

    private final UserGitHubJpaMapper userGitHubJpaMapper;
    private final UserJpaRepository userJpaRepository;
    private final UserGitHubJpaRepository userGitHubJpaRepository;

    @Override
    public UserGitHub save(UserGitHub userGitHub) {

        UserJpaEntity userJpaEntity = userJpaRepository.getReferenceById(userGitHub.userId());
        UserGitHubJpaEntity savedEntity =
                userGitHubJpaRepository.save(userGitHubJpaMapper.toJpaEntity(userGitHub, userJpaEntity));
        return userGitHubJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<UserGitHub> findByUserId(Long userId) {

        return userGitHubJpaRepository.findByUserIdWithUser(userId)
                .map(userGitHubJpaMapper::toDomain);
    }

    @Override
    public Optional<UserGitHub> findByGitHubId(String gitHubId) {

        return userGitHubJpaRepository.findByGitHubIdWithUser(gitHubId)
                .map(userGitHubJpaMapper::toDomain);
    }

    @Override
    public List<AbleRivalInfoForRival> findAbleRivalsWithUserInfo(List<Long> ids) {

        return userGitHubJpaRepository.findAbleRivalsWithUserInfo(ids);
    }

    @Override
    public List<AbleRivalInfoForRival> findAbleRivalsWithUserInfoByKeyword(List<Long> ids, String keyword) {

        return userGitHubJpaRepository.findAbleRivalsWithUserInfoByKeyword(ids, keyword);
    }
}
