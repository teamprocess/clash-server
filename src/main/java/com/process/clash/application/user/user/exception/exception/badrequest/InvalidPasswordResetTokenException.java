package com.process.clash.application.user.user.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.user.user.exception.statuscode.UserStatusCode;

public class InvalidPasswordResetTokenException extends BadRequestException {
    public InvalidPasswordResetTokenException() {
        super(UserStatusCode.INVALID_PASSWORD_RESET_TOKEN);
    }
}
