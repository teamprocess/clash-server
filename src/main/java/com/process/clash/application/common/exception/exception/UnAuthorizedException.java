package com.process.clash.application.common.exception.exception;

import com.process.clash.application.common.exception.statuscode.StatusCode;
import org.springframework.http.HttpStatus;

public abstract class UnAuthorizedException extends ApplicationException {
    protected UnAuthorizedException(StatusCode statusCode) {
        super(statusCode);
    }

    protected UnAuthorizedException(StatusCode statusCode, Throwable cause) {
        super(statusCode, cause);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
