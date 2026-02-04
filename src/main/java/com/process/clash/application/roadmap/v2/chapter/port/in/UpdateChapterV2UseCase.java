package com.process.clash.application.roadmap.v2.chapter.port.in;

import com.process.clash.application.roadmap.v2.chapter.data.UpdateChapterV2Data;

public interface UpdateChapterV2UseCase {
    UpdateChapterV2Data.Result execute(UpdateChapterV2Data.Command command);
}
