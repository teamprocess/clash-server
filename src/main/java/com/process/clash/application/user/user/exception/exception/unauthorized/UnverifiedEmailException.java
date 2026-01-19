package com.process.clash.application.user.user.exception.exception.unauthorized;

import com.process.clash.application.common.exception.exception.UnAuthorizedException;
import com.process.clash.application.user.user.exception.statuscode.UserStatusCode;

public class UnverifiedEmailException extends UnAuthorizedException {
    public UnverifiedEmailException() {
        super(UserStatusCode.UNVERIFIED_EMAIL);
    }

    public UnverifiedEmailException(Throwable cause) {
        super(UserStatusCode.UNVERIFIED_EMAIL, cause);
    }
}
