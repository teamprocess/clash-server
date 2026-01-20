package com.process.clash.application.github.exception.exception.internalserver;

import com.process.clash.application.common.exception.exception.InternalServerException;
import com.process.clash.application.github.exception.statuscode.GithubGraphqlStatusCode;

public class GithubGraphqlQueryLoadException extends InternalServerException {
    public GithubGraphqlQueryLoadException() {
        super(GithubGraphqlStatusCode.GITHUB_GRAPHQL_QUERY_LOAD_FAILED);
    }

    public GithubGraphqlQueryLoadException(Throwable cause) {
        super(GithubGraphqlStatusCode.GITHUB_GRAPHQL_QUERY_LOAD_FAILED, cause);
    }
}
