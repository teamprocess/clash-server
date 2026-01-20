package com.process.clash.application.record.port.in;

import com.process.clash.application.record.data.StopRecordData;

public interface StopRecordUseCase {

    StopRecordData.Result execute(StopRecordData.Command command);
}
