package com.process.clash.application.major.port.in;

import com.process.clash.application.major.data.GetMajorQuestionData;

public interface GetMajorQuestionUseCase {

    GetMajorQuestionData.Result execute(GetMajorQuestionData.Command command);
}
