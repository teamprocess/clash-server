package com.process.clash.application.common.exception.mapper;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import org.springframework.http.HttpStatus;

public final class HttpStatusMapper {

    private HttpStatusMapper() {

    }

    public static HttpStatus toHttpStatus(StatusCode statusCode) {
        ErrorCategory category = statusCode.getErrorCategory();

        return switch (category) {
            case BAD_REQUEST -> HttpStatus.BAD_REQUEST;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT -> HttpStatus.CONFLICT;
            case INTERNAL_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
