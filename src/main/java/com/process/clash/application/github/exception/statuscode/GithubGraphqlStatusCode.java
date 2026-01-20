package com.process.clash.application.github.exception.statuscode;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GithubGraphqlStatusCode implements StatusCode {

    // 500
    GITHUB_GRAPHQL_QUERY_LOAD_FAILED("GITHUB_GRAPHQL_QUERY_LOAD_FAILED", "GitHub GraphQL 쿼리를 불러오지 못했습니다.", ErrorCategory.INTERNAL_ERROR),
    GITHUB_GRAPHQL_REQUEST_FAILED("GITHUB_GRAPHQL_REQUEST_FAILED", "GitHub GraphQL 요청에 실패했습니다.", ErrorCategory.INTERNAL_ERROR),
    GITHUB_GRAPHQL_RESPONSE_ERROR("GITHUB_GRAPHQL_RESPONSE_ERROR", "GitHub GraphQL 응답에 오류가 발생했습니다.", ErrorCategory.INTERNAL_ERROR),
    ;

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
