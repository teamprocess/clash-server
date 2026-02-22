package com.process.clash.application.record.v2.port.in;

import com.process.clash.application.record.v2.data.StartRecordV2Data;

public interface StartRecordV2UseCase {

    StartRecordV2Data.Result execute(StartRecordV2Data.Command command);
}
