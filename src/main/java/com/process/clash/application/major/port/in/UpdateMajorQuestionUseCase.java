package com.process.clash.application.major.port.in;

import com.process.clash.application.major.data.UpdateMajorQuestionData;

public interface UpdateMajorQuestionUseCase {
    UpdateMajorQuestionData.Result execute(UpdateMajorQuestionData.Command command);
}
