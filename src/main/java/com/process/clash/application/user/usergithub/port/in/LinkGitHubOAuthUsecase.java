package com.process.clash.application.user.usergithub.port.in;

import com.process.clash.application.user.usergithub.data.LinkGitHubOAuthData;

public interface LinkGitHubOAuthUsecase {

    LinkGitHubOAuthData.Result execute(LinkGitHubOAuthData.Command command);
}
