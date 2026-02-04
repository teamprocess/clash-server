package com.process.clash.application.roadmap.v2.question.port.in;

import com.process.clash.application.roadmap.v2.question.data.ResetChapterV2Data;

public interface ResetChapterV2UseCase {
    void execute(ResetChapterV2Data.Command command);
}
