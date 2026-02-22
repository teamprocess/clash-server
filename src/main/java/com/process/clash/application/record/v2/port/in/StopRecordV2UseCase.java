package com.process.clash.application.record.v2.port.in;

import com.process.clash.application.record.v2.data.StopRecordV2Data;

public interface StopRecordV2UseCase {

    StopRecordV2Data.Result execute(StopRecordV2Data.Command command);
}
