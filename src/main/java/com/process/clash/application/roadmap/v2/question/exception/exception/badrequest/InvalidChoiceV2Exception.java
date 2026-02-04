package com.process.clash.application.roadmap.v2.question.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.roadmap.v2.question.exception.status.QuestionV2StatusCode;

public class InvalidChoiceV2Exception extends BadRequestException {

    public InvalidChoiceV2Exception() {
        super(QuestionV2StatusCode.INVALID_CHOICE_V2);
    }
}
