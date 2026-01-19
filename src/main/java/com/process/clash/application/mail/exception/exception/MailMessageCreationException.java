package com.process.clash.application.mail.exception.exception;

import com.process.clash.application.common.exception.exception.InternalServerException;
import com.process.clash.application.mail.exception.statuscode.MailStatusCode;

public class MailMessageCreationException extends InternalServerException {
    public MailMessageCreationException() {
        super(MailStatusCode.MAIL_CREATION_FAILED);
    }

    public MailMessageCreationException(Throwable cause) {
        super(MailStatusCode.MAIL_CREATION_FAILED, cause);
    }
}