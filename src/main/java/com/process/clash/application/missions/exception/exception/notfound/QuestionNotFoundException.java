package com.process.clash.application.missions.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.missions.exception.status.MissionStatusCode;

public class QuestionNotFoundException extends NotFoundException {

    public QuestionNotFoundException() {
        super(MissionStatusCode.QUESTION_NOT_FOUND);
    }
}