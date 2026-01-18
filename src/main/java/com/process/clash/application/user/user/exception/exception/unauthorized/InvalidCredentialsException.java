package com.process.clash.application.user.user.exception.exception.unauthorized;

import com.process.clash.application.common.exception.exception.UnAuthorizedException;
import com.process.clash.application.user.user.exception.statuscode.UserStatusCode;

public class InvalidCredentialsException extends UnAuthorizedException {
    public InvalidCredentialsException() {
        super(UserStatusCode.INVALID_CREDENTIALS);
    }

    public InvalidCredentialsException(Throwable cause) {
        super(UserStatusCode.INVALID_CREDENTIALS, cause);
    }
}
