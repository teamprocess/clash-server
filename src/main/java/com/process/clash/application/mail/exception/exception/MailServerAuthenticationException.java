package com.process.clash.application.mail.exception.exception;

import com.process.clash.application.common.exception.exception.InternalServerException;
import com.process.clash.application.mail.exception.statuscode.MailStatusCode;

public class MailServerAuthenticationException extends InternalServerException {
    public MailServerAuthenticationException() {
        super(MailStatusCode.MAIL_SERVER_AUTHENTICATION);
    }

    public MailServerAuthenticationException(Throwable cause) {
        super(MailStatusCode.MAIL_SERVER_AUTHENTICATION, cause);
    }
}
