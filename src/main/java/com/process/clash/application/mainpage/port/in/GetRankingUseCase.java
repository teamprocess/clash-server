package com.process.clash.application.mainpage.port.in;

import com.process.clash.application.mainpage.data.GetRankingData;

public interface GetRankingUseCase {

    GetRankingData.Result execute(GetRankingData.Command command);
}
