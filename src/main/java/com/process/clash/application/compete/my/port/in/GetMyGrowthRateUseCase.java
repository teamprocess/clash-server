package com.process.clash.application.compete.my.port.in;

import com.process.clash.application.compete.my.data.GetMyGrowthRateData;

public interface GetMyGrowthRateUseCase {

    GetMyGrowthRateData.Result execute(GetMyGrowthRateData.Command command);
}
