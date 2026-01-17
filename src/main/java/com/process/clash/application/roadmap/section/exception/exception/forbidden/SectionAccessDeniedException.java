package com.process.clash.application.roadmap.section.exception.exception.forbidden;

import com.process.clash.application.common.exception.exception.ForbiddenException;
import com.process.clash.application.common.exception.statuscode.CommonStatusCode;

public class SectionAccessDeniedException extends ForbiddenException {
    public SectionAccessDeniedException() {
        super(CommonStatusCode.PERMISSION_DENIED);
    }

    public SectionAccessDeniedException(Throwable cause) {
        super(CommonStatusCode.PERMISSION_DENIED, cause);
    }
}
