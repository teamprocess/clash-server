package com.process.clash.application.user.user.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.user.user.exception.statuscode.UserStatusCode;

public class VerificationCodeExpiredOrWrongEmailException extends BadRequestException {
    public VerificationCodeExpiredOrWrongEmailException() {
        super(UserStatusCode.VERIFICATION_CODE_EXPIRED_OR_WRONG_EMAIL);
    }

    public VerificationCodeExpiredOrWrongEmailException(Throwable cause) {
        super(UserStatusCode.VERIFICATION_CODE_EXPIRED_OR_WRONG_EMAIL, cause);
    }
}
