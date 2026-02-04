package com.process.clash.application.roadmap.v2.chapter.port.in;

import com.process.clash.application.roadmap.v2.chapter.data.GetChapterV2DetailsData;

public interface GetChapterV2DetailsUseCase {
    GetChapterV2DetailsData.Result execute(GetChapterV2DetailsData.Command command);
}
