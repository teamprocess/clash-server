package com.process.clash.application.major.port.in;

import com.process.clash.application.major.data.PostMajorQuestionData;

public interface PostMajorQuestionUseCase {
    PostMajorQuestionData.Result execute(PostMajorQuestionData.Command command);
}
