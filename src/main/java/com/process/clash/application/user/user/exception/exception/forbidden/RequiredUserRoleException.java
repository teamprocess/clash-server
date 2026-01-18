package com.process.clash.application.user.user.exception.exception.forbidden;

import com.process.clash.application.common.exception.exception.ForbiddenException;
import com.process.clash.application.user.user.exception.statuscode.UserStatusCode;

public class RequiredUserRoleException extends ForbiddenException {
    public RequiredUserRoleException() {
        super(UserStatusCode.REQUIRED_USER_ROLE);
    }

    public RequiredUserRoleException(Throwable cause) {
        super(UserStatusCode.REQUIRED_USER_ROLE, cause);
    }
}
