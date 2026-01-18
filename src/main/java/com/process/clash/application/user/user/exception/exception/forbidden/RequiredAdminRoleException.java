package com.process.clash.application.user.user.exception.exception.forbidden;

import com.process.clash.application.common.exception.exception.ForbiddenException;
import com.process.clash.application.user.user.exception.statuscode.UserStatusCode;

public class RequiredAdminRoleException extends ForbiddenException {
    public RequiredAdminRoleException() {
        super(UserStatusCode.REQUIRED_ADMIN_ROLE);
    }

    public RequiredAdminRoleException(Throwable cause) {
        super(UserStatusCode.REQUIRED_ADMIN_ROLE, cause);
    }
}
