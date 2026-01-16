package com.process.clash.application.roadmap.sectionprogress.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.roadmap.sectionprogress.exception.status.UserSectionProgressStatusCode;

public class UserSectionProgressNotFoundException extends NotFoundException {
    public UserSectionProgressNotFoundException() {
        super(UserSectionProgressStatusCode.USER_SECTION_PROGRESS_NOT_FOUND);
    }

    public UserSectionProgressNotFoundException(Throwable cause) {
        super(UserSectionProgressStatusCode.USER_SECTION_PROGRESS_NOT_FOUND, cause);
    }
}
