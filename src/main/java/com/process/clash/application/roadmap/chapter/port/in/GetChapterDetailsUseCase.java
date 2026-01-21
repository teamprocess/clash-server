package com.process.clash.application.roadmap.chapter.port.in;

import com.process.clash.application.roadmap.chapter.data.GetChapterDetailsData;

public interface GetChapterDetailsUseCase {
    GetChapterDetailsData.Result execute(GetChapterDetailsData.Command command);
}
