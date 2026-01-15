package com.process.clash.application.user.user.exception.exception.internalserver;

import com.process.clash.application.common.exception.exception.InternalServerException;
import com.process.clash.application.common.exception.statuscode.CommonStatusCode;

public class AccessContextMissingException extends InternalServerException {
    public AccessContextMissingException() {
        super(CommonStatusCode.SERVLET_CONTEXT_UNAVAILABLE);
    }

    public AccessContextMissingException(Throwable cause) {
        super(CommonStatusCode.SERVLET_CONTEXT_UNAVAILABLE, cause);
    }
}
