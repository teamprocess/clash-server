package com.process.clash.application.major.port.in;

import com.process.clash.application.major.data.GetMajorQuestionCommand;
import com.process.clash.application.major.data.GetMajorQuestionResult;

public interface GetMajorQuestionUseCase {

    GetMajorQuestionResult findAll(GetMajorQuestionCommand command);
}
