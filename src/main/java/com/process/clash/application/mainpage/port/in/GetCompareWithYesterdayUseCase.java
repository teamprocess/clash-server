package com.process.clash.application.mainpage.port.in;

import com.process.clash.application.mainpage.data.GetCompareWithYesterdayData;

public interface GetCompareWithYesterdayUseCase {

    GetCompareWithYesterdayData.Result execute(GetCompareWithYesterdayData.Command command);
}
