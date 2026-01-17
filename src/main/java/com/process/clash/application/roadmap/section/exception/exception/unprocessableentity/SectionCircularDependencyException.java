package com.process.clash.application.roadmap.section.exception.exception.unprocessableentity;

import com.process.clash.application.common.exception.exception.UnprocessableEntityException;
import com.process.clash.application.roadmap.section.exception.status.SectionStatusCode;

public class SectionCircularDependencyException extends UnprocessableEntityException {

    public SectionCircularDependencyException() {
        super(SectionStatusCode.SECTION_CIRCULAR_DEPENDENCY);
    }

    protected SectionCircularDependencyException(Throwable cause) {
        super(SectionStatusCode.SECTION_CIRCULAR_DEPENDENCY, cause);
    }
}
