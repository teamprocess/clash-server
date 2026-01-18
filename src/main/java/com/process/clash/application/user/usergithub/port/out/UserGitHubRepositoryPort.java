package com.process.clash.application.user.usergithub.port.out;

import com.process.clash.domain.user.usergithub.entity.UserGitHub;

import java.util.List;

public interface UserGitHubRepositoryPort {

    UserGitHub save(UserGitHub userGitHub);
    List<UserGitHub> findByUserIdNotIn(List<Long> ids);
}
