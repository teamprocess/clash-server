package com.process.clash.application.roadmap.v2.chapter.port.in;

import com.process.clash.application.roadmap.v2.chapter.data.CreateChapterV2Data;

public interface CreateChapterV2UseCase {
    CreateChapterV2Data.Result execute(CreateChapterV2Data.Command command);
}
