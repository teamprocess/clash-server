package com.process.clash.application.mainpage.port.in.mainpage;

import com.process.clash.application.compete.my.data.GetCompareWithYesterdayData;

public interface GetCompareWithYesterdayUseCase {

    GetCompareWithYesterdayData.Result execute(GetCompareWithYesterdayData.Command command);
}
