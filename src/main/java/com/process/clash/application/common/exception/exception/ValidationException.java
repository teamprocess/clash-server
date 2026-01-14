package com.process.clash.application.common.exception.exception;

import com.process.clash.application.common.exception.statuscode.CommonStatusCode;

public class ValidationException extends BadRequestException {

    public ValidationException() {
        super(CommonStatusCode.INVALID_ARGUMENT);
    }

    public ValidationException(Throwable cause) {
        super(CommonStatusCode.INVALID_ARGUMENT, cause);
    }

}