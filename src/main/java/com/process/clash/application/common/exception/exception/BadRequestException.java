package com.process.clash.application.common.exception.exception;

import com.process.clash.application.common.exception.statuscode.StatusCode;
import org.springframework.http.HttpStatus;

public abstract class BadRequestException extends ApplicationException {
    protected BadRequestException(StatusCode statusCode) {
        super(statusCode);
    }

    protected BadRequestException(StatusCode statusCode, Throwable cause) {
        super(statusCode, cause);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
