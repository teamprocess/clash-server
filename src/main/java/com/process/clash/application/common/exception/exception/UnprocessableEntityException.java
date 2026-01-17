package com.process.clash.application.common.exception.exception;

import com.process.clash.application.common.exception.statuscode.StatusCode;
import org.springframework.http.HttpStatus;

public abstract class UnprocessableEntityException extends ApplicationException {
    protected UnprocessableEntityException(StatusCode statusCode) {
        super(statusCode);
    }

    protected UnprocessableEntityException(StatusCode statusCode, Throwable cause) {
        super(statusCode, cause);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
