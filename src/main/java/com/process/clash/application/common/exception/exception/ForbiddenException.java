package com.process.clash.application.common.exception.exception;

import com.process.clash.application.common.exception.statuscode.StatusCode;
import org.springframework.http.HttpStatus;

public abstract class ForbiddenException extends ApplicationException {
    protected ForbiddenException(StatusCode statusCode) {
        super(statusCode);
    }

    protected ForbiddenException(StatusCode statusCode, Throwable cause) {
        super(statusCode, cause);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
