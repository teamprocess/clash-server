package com.process.clash.application.auth.electron.exception.exception.badrequest;

import com.process.clash.application.auth.electron.exception.statuscode.ElectronAuthStatusCode;
import com.process.clash.application.common.exception.exception.BadRequestException;

public class InvalidRedirectUriException extends BadRequestException {
    public InvalidRedirectUriException() {
        super(ElectronAuthStatusCode.INVALID_REDIRECT_URI);
    }

    public InvalidRedirectUriException(Throwable cause) {
        super(ElectronAuthStatusCode.INVALID_REDIRECT_URI, cause);
    }
}
