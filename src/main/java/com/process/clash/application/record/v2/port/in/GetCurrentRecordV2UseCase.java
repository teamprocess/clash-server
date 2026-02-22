package com.process.clash.application.record.v2.port.in;

import com.process.clash.application.record.v2.data.GetCurrentRecordV2Data;

public interface GetCurrentRecordV2UseCase {

    GetCurrentRecordV2Data.Result execute(GetCurrentRecordV2Data.Command command);
}
