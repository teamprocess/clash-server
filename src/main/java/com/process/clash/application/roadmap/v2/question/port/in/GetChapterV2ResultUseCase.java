package com.process.clash.application.roadmap.v2.question.port.in;

import com.process.clash.application.roadmap.v2.question.data.GetChapterV2ResultData;

public interface GetChapterV2ResultUseCase {
    GetChapterV2ResultData.Result execute(GetChapterV2ResultData.Command command);
}
