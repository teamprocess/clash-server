package com.process.clash.application.roadmap.v2.question.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.roadmap.v2.question.exception.status.QuestionV2StatusCode;

public class InvalidQuestionOrderV2Exception extends BadRequestException {

    public InvalidQuestionOrderV2Exception() {
        super(QuestionV2StatusCode.INVALID_QUESTION_ORDER_V2);
    }
}
