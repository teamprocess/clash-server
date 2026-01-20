package com.process.clash.application.record.port.in;

import com.process.clash.application.record.data.StartRecordData;

public interface StartRecordUseCase {

    StartRecordData.Result execute(StartRecordData.Command command);
}
