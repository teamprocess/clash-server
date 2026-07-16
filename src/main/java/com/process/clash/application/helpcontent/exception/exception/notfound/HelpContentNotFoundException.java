package com.process.clash.application.helpcontent.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.helpcontent.exception.statuscode.HelpContentStatusCode;

public class HelpContentNotFoundException extends NotFoundException {

    public HelpContentNotFoundException() {
        super(HelpContentStatusCode.HELP_CONTENT_NOT_FOUND);
    }
}
