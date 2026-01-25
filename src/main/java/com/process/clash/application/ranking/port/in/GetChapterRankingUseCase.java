package com.process.clash.application.ranking.port.in;

import com.process.clash.application.ranking.data.GetChapterRankingData;

public interface GetChapterRankingUseCase {
    GetChapterRankingData.Result execute(GetChapterRankingData.Command command);
}
