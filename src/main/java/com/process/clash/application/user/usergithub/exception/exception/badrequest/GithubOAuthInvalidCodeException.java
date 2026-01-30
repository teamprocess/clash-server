package com.process.clash.application.user.usergithub.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.user.usergithub.exception.statuscode.UserGitHubStatusCode;

public class GithubOAuthInvalidCodeException extends BadRequestException {

    public GithubOAuthInvalidCodeException() {
        super(UserGitHubStatusCode.GITHUB_OAUTH_INVALID_CODE);
    }

    public GithubOAuthInvalidCodeException(Throwable cause) {
        super(UserGitHubStatusCode.GITHUB_OAUTH_INVALID_CODE, cause);
    }
}
