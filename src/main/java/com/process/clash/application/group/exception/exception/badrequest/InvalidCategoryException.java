package com.process.clash.application.group.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.group.exception.statuscode.GroupStatusCode;

public class InvalidCategoryException extends BadRequestException {
    private final String customMessage;

    public InvalidCategoryException() {
        super(GroupStatusCode.INVALID_CATEGORY);
        this.customMessage = null;
    }

    public InvalidCategoryException(String customMessage) {
        super(GroupStatusCode.INVALID_CATEGORY);
        this.customMessage = customMessage;
    }

    public InvalidCategoryException(Throwable cause) {
        super(GroupStatusCode.INVALID_CATEGORY, cause);
        this.customMessage = null;
    }

    @Override
    public String getMessage() {
        return customMessage != null ? customMessage : super.getMessage();
    }
}
