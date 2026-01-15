package com.process.clash.application.compete.my.port.in;

import com.process.clash.application.compete.my.data.GetCompareWithYesterdayData;

public interface GetCompareWithYesterdayUseCase {

    GetCompareWithYesterdayData.Result execute(GetCompareWithYesterdayData.Command command);
}
