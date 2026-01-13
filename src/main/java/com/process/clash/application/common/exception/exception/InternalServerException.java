package com.process.clash.application.common.exception.exception;

import com.process.clash.application.common.exception.statuscode.StatusCode;
import org.springframework.http.HttpStatus;

public abstract class InternalServerException extends ApplicationException {
    protected InternalServerException(StatusCode statusCode) {
        super(statusCode);
    }

    protected InternalServerException(StatusCode statusCode, Throwable cause) {
        super(statusCode, cause);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
