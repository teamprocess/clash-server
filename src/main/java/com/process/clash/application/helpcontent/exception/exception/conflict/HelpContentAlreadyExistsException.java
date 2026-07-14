package com.process.clash.application.helpcontent.exception.exception.conflict;

import com.process.clash.application.common.exception.exception.ConflictException;
import com.process.clash.application.helpcontent.exception.statuscode.HelpContentStatusCode;

public class HelpContentAlreadyExistsException extends ConflictException {

    public HelpContentAlreadyExistsException() {
        super(HelpContentStatusCode.HELP_CONTENT_ALREADY_EXISTS);
    }
}
