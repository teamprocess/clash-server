package com.process.clash.application.major.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.major.exception.status.MajorStatusCode;

public class MajorQuestionNotFoundException extends NotFoundException {
    public MajorQuestionNotFoundException() {
        super(MajorStatusCode.MAJOR_QUESTION_NOT_FOUND);
    }
}
