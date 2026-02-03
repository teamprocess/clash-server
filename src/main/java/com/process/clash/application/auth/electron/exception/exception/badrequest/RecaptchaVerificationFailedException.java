package com.process.clash.application.auth.electron.exception.exception.badrequest;

import com.process.clash.application.auth.electron.exception.statuscode.ElectronAuthStatusCode;
import com.process.clash.application.common.exception.exception.BadRequestException;

public class RecaptchaVerificationFailedException extends BadRequestException {
    public RecaptchaVerificationFailedException() {
        super(ElectronAuthStatusCode.RECAPTCHA_VERIFICATION_FAILED);
    }

    public RecaptchaVerificationFailedException(Throwable cause) {
        super(ElectronAuthStatusCode.RECAPTCHA_VERIFICATION_FAILED, cause);
    }
}
