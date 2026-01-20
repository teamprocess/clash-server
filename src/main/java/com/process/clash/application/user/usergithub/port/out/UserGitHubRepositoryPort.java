package com.process.clash.application.user.usergithub.port.out;

import com.process.clash.application.compete.rival.rival.data.AbleRivalInfo;
import com.process.clash.domain.user.usergithub.entity.UserGitHub;

import java.util.List;

public interface UserGitHubRepositoryPort {

    UserGitHub save(UserGitHub userGitHub);
    List<AbleRivalInfo> findAbleRivalsWithUserInfo(List<Long> ids);
    List<AbleRivalInfo> findAbleRivalsWithUserInfoByKeyword(List<Long> ids, String keyword);
}
