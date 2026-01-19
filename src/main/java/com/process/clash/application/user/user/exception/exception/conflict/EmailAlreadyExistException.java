package com.process.clash.application.user.user.exception.exception.conflict;

import com.process.clash.application.common.exception.exception.ConflictException;
import com.process.clash.application.user.user.exception.statuscode.UserStatusCode;

public class EmailAlreadyExistException extends ConflictException {
    public EmailAlreadyExistException() {
        super(UserStatusCode.EMAIL_ALREADY_EXIST);
    }
}