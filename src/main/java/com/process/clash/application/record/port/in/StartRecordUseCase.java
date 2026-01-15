package com.process.clash.application.record.port.in;

import com.process.clash.application.record.dto.StartRecordData;

public interface StartRecordUseCase {

    StartRecordData.Result execute(StartRecordData.Command command);
}
