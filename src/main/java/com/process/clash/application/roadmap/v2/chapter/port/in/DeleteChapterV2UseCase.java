package com.process.clash.application.roadmap.v2.chapter.port.in;

import com.process.clash.application.roadmap.v2.chapter.data.DeleteChapterV2Data;

public interface DeleteChapterV2UseCase {
    void execute(DeleteChapterV2Data.Command command);
}
