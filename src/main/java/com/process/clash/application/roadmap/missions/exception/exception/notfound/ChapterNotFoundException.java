package com.process.clash.application.roadmap.missions.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.roadmap.missions.exception.status.MissionStatusCode;

public class ChapterNotFoundException extends NotFoundException {

    public ChapterNotFoundException() {
        super(MissionStatusCode.CHAPTER_NOT_FOUND);
    }
}