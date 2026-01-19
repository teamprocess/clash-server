package com.process.clash.application.mail.exception.exception;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.mail.exception.statuscode.MailStatusCode;

public class InvalidMailException extends BadRequestException {
    public InvalidMailException() {
        super(MailStatusCode.INVALID_MAIL_ADDRESS);
    }

    public InvalidMailException(Throwable cause) {
        super(MailStatusCode.INVALID_MAIL_ADDRESS, cause);
    }
}
