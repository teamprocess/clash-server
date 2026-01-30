package com.process.clash.application.user.usergithub.exception.exception.conflict;

import com.process.clash.application.common.exception.exception.ConflictException;
import com.process.clash.application.user.usergithub.exception.statuscode.UserGitHubStatusCode;

public class GithubAlreadyLinkedException extends ConflictException {

    public GithubAlreadyLinkedException() {
        super(UserGitHubStatusCode.GITHUB_ALREADY_LINKED);
    }

    public GithubAlreadyLinkedException(Throwable cause) {
        super(UserGitHubStatusCode.GITHUB_ALREADY_LINKED, cause);
    }
}
