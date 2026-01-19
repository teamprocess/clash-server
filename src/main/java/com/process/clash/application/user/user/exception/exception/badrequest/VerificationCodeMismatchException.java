package com.process.clash.application.user.user.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.user.user.exception.statuscode.UserStatusCode;

public class VerificationCodeMismatchException extends BadRequestException {
    public VerificationCodeMismatchException() {
        super(UserStatusCode.VERIFICATION_CODE_MISMATCH);
    }

    public VerificationCodeMismatchException(Throwable cause) {
        super(UserStatusCode.VERIFICATION_CODE_MISMATCH, cause);
    }
}
