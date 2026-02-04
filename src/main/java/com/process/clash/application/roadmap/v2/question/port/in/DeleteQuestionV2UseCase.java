package com.process.clash.application.roadmap.v2.question.port.in;

import com.process.clash.application.roadmap.v2.question.data.DeleteQuestionV2Data;

public interface DeleteQuestionV2UseCase {
    void execute(DeleteQuestionV2Data.Command command);
}
