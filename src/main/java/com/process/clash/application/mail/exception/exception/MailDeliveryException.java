package com.process.clash.application.mail.exception.exception;

import com.process.clash.application.common.exception.exception.InternalServerException;
import com.process.clash.application.mail.exception.statuscode.MailStatusCode;

public class MailDeliveryException extends InternalServerException {
    public MailDeliveryException() {
        super(MailStatusCode.MAIL_SEND_INTERRUPTED);
    }

    public MailDeliveryException(Throwable cause) {
        super(MailStatusCode.MAIL_SEND_INTERRUPTED, cause);
    }
}