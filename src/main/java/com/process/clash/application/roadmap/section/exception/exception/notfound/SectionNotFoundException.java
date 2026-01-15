package com.process.clash.application.roadmap.section.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.roadmap.section.exception.status.SectionStatusCode;

public class SectionNotFoundException extends NotFoundException {
    public SectionNotFoundException() {
        super(SectionStatusCode.SECTION_NOT_FOUND);
    }

    public SectionNotFoundException(Throwable cause) {
        super(SectionStatusCode.SECTION_NOT_FOUND, cause);
    }
}
