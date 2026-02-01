package com.process.clash.application.user.usergithub.exception.exception.internalserver;

import com.process.clash.application.common.exception.exception.InternalServerException;
import com.process.clash.application.user.usergithub.exception.statuscode.UserGitHubStatusCode;

public class GithubOAuthUserFetchFailedException extends InternalServerException {

    public GithubOAuthUserFetchFailedException() {
        super(UserGitHubStatusCode.GITHUB_OAUTH_USER_FETCH_FAILED);
    }

    public GithubOAuthUserFetchFailedException(Throwable cause) {
        super(UserGitHubStatusCode.GITHUB_OAUTH_USER_FETCH_FAILED, cause);
    }
}
