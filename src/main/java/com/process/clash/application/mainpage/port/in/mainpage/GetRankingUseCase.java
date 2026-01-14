package com.process.clash.application.mainpage.port.in.mainpage;

import com.process.clash.application.mainpage.data.mainpage.GetRankingData;

public interface GetRankingUseCase {

    GetRankingData.Result execute(GetRankingData.Command command);
}
