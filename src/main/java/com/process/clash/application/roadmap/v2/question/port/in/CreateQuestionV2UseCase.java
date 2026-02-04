package com.process.clash.application.roadmap.v2.question.port.in;

import com.process.clash.application.roadmap.v2.question.data.CreateQuestionV2Data;

public interface CreateQuestionV2UseCase {
    CreateQuestionV2Data.Result execute(CreateQuestionV2Data.Command command);
}
