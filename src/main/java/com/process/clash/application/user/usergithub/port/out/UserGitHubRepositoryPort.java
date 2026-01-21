package com.process.clash.application.user.usergithub.port.out;

import com.process.clash.application.compete.rival.rival.data.AbleRivalInfoForRival;
import com.process.clash.domain.user.usergithub.entity.UserGitHub;

import java.util.List;

public interface UserGitHubRepositoryPort {

    UserGitHub save(UserGitHub userGitHub);
    List<AbleRivalInfoForRival> findAbleRivalsWithUserInfo(List<Long> ids);
    List<AbleRivalInfoForRival> findAbleRivalsWithUserInfoByKeyword(List<Long> ids, String keyword);
}
