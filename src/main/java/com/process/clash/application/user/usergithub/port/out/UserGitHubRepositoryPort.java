package com.process.clash.application.user.usergithub.port.out;

import com.process.clash.domain.user.usergithub.entity.UserGitHub;

public interface UserGitHubRepositoryPort {

    UserGitHub save(UserGitHub userGitHub);
}
