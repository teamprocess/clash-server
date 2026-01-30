package com.process.clash.application.user.usergithub.port.out;

import com.process.clash.application.compete.rival.rival.data.AbleRivalInfoForRival;
import com.process.clash.domain.user.usergithub.entity.UserGitHub;

import java.util.List;
import java.util.Optional;

public interface UserGitHubRepositoryPort {

    UserGitHub save(UserGitHub userGitHub);
    Optional<UserGitHub> findByUserId(Long userId);
    Optional<UserGitHub> findByGitHubId(String gitHubId);
    List<AbleRivalInfoForRival> findAbleRivalsWithUserInfoByKeyword(List<Long> ids, String keyword);
}
