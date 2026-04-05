package com.process.clash.application.announcement.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.announcement.exception.statuscode.AnnouncementStatusCode;

public class AnnouncementNotFoundException extends NotFoundException {

    public AnnouncementNotFoundException() {
        super(AnnouncementStatusCode.ANNOUNCEMENT_NOT_FOUND);
    }
}
