package com.process.clash.application.record.v2.port.in;

import com.process.clash.application.record.v2.data.GetActivityStatisticsV2Data;

public interface GetActivityStatisticsV2UseCase {

    GetActivityStatisticsV2Data.Result execute(GetActivityStatisticsV2Data.Command command);
}
