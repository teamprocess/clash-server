package com.process.clash.application.roadmap.missions.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.roadmap.missions.exception.status.MissionStatusCode;

public class InvalidChoiceException extends BadRequestException {

    public InvalidChoiceException() {
        super(MissionStatusCode.INVALID_CHOICE);
    }
}