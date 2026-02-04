package com.process.clash.application.roadmap.v2.question.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.roadmap.v2.question.exception.status.QuestionV2StatusCode;

public class ChoiceV2NotFoundException extends NotFoundException {

    public ChoiceV2NotFoundException() {
        super(QuestionV2StatusCode.CHOICE_V2_NOT_FOUND);
    }
}
