package com.process.clash.application.github.exception.exception.internalserver;

import com.process.clash.application.common.exception.exception.InternalServerException;
import com.process.clash.application.github.exception.statuscode.GithubGraphqlStatusCode;

public class GithubGraphqlRequestFailedException extends InternalServerException {
    public GithubGraphqlRequestFailedException() {
        super(GithubGraphqlStatusCode.GITHUB_GRAPHQL_REQUEST_FAILED);
    }

    public GithubGraphqlRequestFailedException(Throwable cause) {
        super(GithubGraphqlStatusCode.GITHUB_GRAPHQL_REQUEST_FAILED, cause);
    }
}
