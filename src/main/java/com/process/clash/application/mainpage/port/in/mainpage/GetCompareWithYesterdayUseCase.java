package com.process.clash.application.mainpage.port.in.mainpage;

import com.process.clash.application.mainpage.data.mainpage.GetCompareWithYesterdayData;

public interface GetCompareWithYesterdayUseCase {

    GetCompareWithYesterdayData.Result execute(GetCompareWithYesterdayData.Command command);
}
