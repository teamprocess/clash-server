package com.process.clash.application.user.user.exception.exception.validation;

import com.process.clash.application.common.exception.exception.ValidationException;

public class AccessContextMissingException extends ValidationException {
    public AccessContextMissingException() {
        super();
    }

    public AccessContextMissingException(Throwable cause) {
        super(cause);
    }
}
