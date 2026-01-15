package com.process.clash.application.ranking.port.in;

import com.process.clash.application.ranking.data.GetRankingData;

public interface GetRankingUseCase {

    GetRankingData.Result execute(GetRankingData.Command command);
}
