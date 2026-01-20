package com.process.clash.application.roadmap.missions.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.roadmap.missions.exception.status.MissionStatusCode;

public class MissionNotFoundException extends NotFoundException {

    public MissionNotFoundException() {
        super(MissionStatusCode.MISSION_NOT_FOUND);
    }
}