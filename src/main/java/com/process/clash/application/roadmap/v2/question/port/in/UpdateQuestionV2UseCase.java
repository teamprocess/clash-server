package com.process.clash.application.roadmap.v2.question.port.in;

import com.process.clash.application.roadmap.v2.question.data.UpdateQuestionV2Data;

public interface UpdateQuestionV2UseCase {
    UpdateQuestionV2Data.Result execute(UpdateQuestionV2Data.Command command);
}
