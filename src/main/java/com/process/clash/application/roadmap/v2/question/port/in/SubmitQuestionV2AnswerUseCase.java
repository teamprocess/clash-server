package com.process.clash.application.roadmap.v2.question.port.in;

import com.process.clash.application.roadmap.v2.question.data.SubmitQuestionV2AnswerData;

public interface SubmitQuestionV2AnswerUseCase {
    SubmitQuestionV2AnswerData.Result execute(SubmitQuestionV2AnswerData.Command command);
}
