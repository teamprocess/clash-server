package com.process.clash.application.major.port.in;

import com.process.clash.application.major.data.DeleteMajorQuestionData;

public interface DeleteMajorQuestionUseCase {
    void execute(DeleteMajorQuestionData.Command command);
}
