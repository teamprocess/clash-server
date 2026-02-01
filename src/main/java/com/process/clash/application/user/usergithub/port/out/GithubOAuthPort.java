package com.process.clash.application.user.usergithub.port.out;

import com.process.clash.application.user.usergithub.model.GithubOAuthToken;
import com.process.clash.application.user.usergithub.model.GithubOAuthUser;

import java.util.List;

public interface GithubOAuthPort {

    GithubOAuthToken exchangeCodeForAccessToken(String code);

    GithubOAuthUser fetchUser(String accessToken);

    List<String> fetchEmails(String accessToken);
}
