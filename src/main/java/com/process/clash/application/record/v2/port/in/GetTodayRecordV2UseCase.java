package com.process.clash.application.record.v2.port.in;

import com.process.clash.application.record.v2.data.GetTodayRecordV2Data;

public interface GetTodayRecordV2UseCase {

    GetTodayRecordV2Data.Result execute(GetTodayRecordV2Data.Command command);
}
