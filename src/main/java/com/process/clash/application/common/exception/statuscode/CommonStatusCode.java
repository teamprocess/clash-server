package com.process.clash.application.common.exception.statuscode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonStatusCode implements StatusCode {

    ENTITY_REFRESH_FAILED(
            "ENTITY_REFRESH_FAILED",
            "엔티티 새로고침에 실패했습니다.",
            ErrorCategory.INTERNAL_ERROR
    ),

    SERVLET_CONTEXT_UNAVAILABLE(
            "SERVLET_CONTEXT_UNAVAILABLE",
            "서블릿 컨텍스트를 사용할 수 없습니다.",
            ErrorCategory.INTERNAL_ERROR
    ),

    INVALID_ARGUMENT(
            "INVALID_ARGUMENT",
            "유효하지 않은 인자입니다.",
            ErrorCategory.BAD_REQUEST
    ),

    ENDPOINT_NOT_FOUND(
            "ENDPOINT_NOT_FOUND",
            "존재하지 않는 엔드포인트입니다.",
            ErrorCategory.NOT_FOUND
    ),

    METHOD_NOT_ALLOWED(
            "METHOD_NOT_ALLOWED",
            "지원하지 않는 HTTP 메서드입니다.",
            ErrorCategory.METHOD_NOT_ALLOWED
    ),

    ENDPOINT_MOVED(
            "ENDPOINT_MOVED",
            "엔드포인트가 변경되었습니다.",
            ErrorCategory.GONE
    ),

    UNAUTHORIZED(
            "UNAUTHORIZED",
            "인증이 필요한 서비스입니다.", // common으로 쓰기 모호한 느낌의 메세지
            ErrorCategory.UNAUTHORIZED
    ),

    PERMISSION_DENIED(
            "PERMISSION_DENIED",
            "해당 작업을 수행할 권한이 없습니다.",
            ErrorCategory.FORBIDDEN
    ),

    INTERNAL_SERVER_ERROR(
            "INTERNAL_SERVER_ERROR",
            "서버에 오류가 발생했습니다.",
            ErrorCategory.INTERNAL_ERROR
    );

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
