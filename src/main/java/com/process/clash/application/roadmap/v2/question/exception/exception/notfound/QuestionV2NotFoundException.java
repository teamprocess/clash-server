package com.process.clash.application.roadmap.v2.question.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.roadmap.v2.question.exception.status.QuestionV2StatusCode;

public class QuestionV2NotFoundException extends NotFoundException {

    public QuestionV2NotFoundException() {
        super(QuestionV2StatusCode.QUESTION_V2_NOT_FOUND);
    }
}
