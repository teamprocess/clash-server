package com.process.clash.application.common.exception.statuscode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonStatusCode implements StatusCode {

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
