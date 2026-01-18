package com.process.clash.application.roadmap.section.exception.exception.forbidden;

import com.process.clash.application.common.exception.exception.ForbiddenException;
import com.process.clash.application.roadmap.section.exception.status.SectionStatusCode;

public class SectionAccessDeniedException extends ForbiddenException {
    public SectionAccessDeniedException() {
        super(SectionStatusCode.SECTION_PREREQUISITE_NOT_COMPLETED);
    }

    public SectionAccessDeniedException(Throwable cause) {
        super(SectionStatusCode.SECTION_PREREQUISITE_NOT_COMPLETED, cause);
    }
}
