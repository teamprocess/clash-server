package com.process.clash.application.user.user.exception.exception.conflict;

import com.process.clash.application.common.exception.exception.ConflictException;
import com.process.clash.application.user.user.exception.statuscode.UserStatusCode;

public class UsernameAlreadyExistException extends ConflictException {
    public UsernameAlreadyExistException() {
        super(UserStatusCode.USERNAME_ALREADY_EXIST);
    }

    public UsernameAlreadyExistException(Throwable cause) {
        super(UserStatusCode.USERNAME_ALREADY_EXIST, cause);
    }
}
