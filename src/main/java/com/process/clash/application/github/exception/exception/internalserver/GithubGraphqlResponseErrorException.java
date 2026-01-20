package com.process.clash.application.github.exception.exception.internalserver;

import com.process.clash.application.common.exception.exception.InternalServerException;
import com.process.clash.application.github.exception.statuscode.GithubGraphqlStatusCode;

public class GithubGraphqlResponseErrorException extends InternalServerException {
    public GithubGraphqlResponseErrorException() {
        super(GithubGraphqlStatusCode.GITHUB_GRAPHQL_RESPONSE_ERROR);
    }

    public GithubGraphqlResponseErrorException(Throwable cause) {
        super(GithubGraphqlStatusCode.GITHUB_GRAPHQL_RESPONSE_ERROR, cause);
    }
}
