package com.process.clash.application.user.usergithub.exception.exception.internalserver;

import com.process.clash.application.common.exception.exception.InternalServerException;
import com.process.clash.application.user.usergithub.exception.statuscode.UserGitHubStatusCode;

public class GithubOAuthTokenRequestFailedException extends InternalServerException {

    public GithubOAuthTokenRequestFailedException() {
        super(UserGitHubStatusCode.GITHUB_OAUTH_TOKEN_REQUEST_FAILED);
    }

    public GithubOAuthTokenRequestFailedException(Throwable cause) {
        super(UserGitHubStatusCode.GITHUB_OAUTH_TOKEN_REQUEST_FAILED, cause);
    }
}
