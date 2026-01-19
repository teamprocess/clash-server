package com.process.clash.application.githubinfo.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.githubinfo.exception.statuscode.GitHubInfoStatusCode;
import com.process.clash.application.user.userstudytime.exception.statuscode.UserStudyTimeStatusCode;

public class GitHubInfoNotFoundException extends NotFoundException {
    public GitHubInfoNotFoundException() {
        super(GitHubInfoStatusCode.GIT_HUB_INFO_NOT_FOUND);
    }

    public GitHubInfoNotFoundException(Throwable cause) {
        super(GitHubInfoStatusCode.GIT_HUB_INFO_NOT_FOUND, cause);
    }
}
