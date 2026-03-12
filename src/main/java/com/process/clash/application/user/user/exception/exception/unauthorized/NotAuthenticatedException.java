package com.process.clash.application.user.user.exception.exception.unauthorized;

import com.process.clash.application.common.exception.exception.UnAuthorizedException;
import com.process.clash.application.user.user.exception.statuscode.UserStatusCode;

public class NotAuthenticatedException extends UnAuthorizedException {
    public NotAuthenticatedException() {
        super(UserStatusCode.NOT_AUTHENTICATED);
    }
}
