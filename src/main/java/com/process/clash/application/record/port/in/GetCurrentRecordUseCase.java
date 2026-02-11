package com.process.clash.application.record.port.in;

import com.process.clash.application.record.data.GetCurrentRecordData;

public interface GetCurrentRecordUseCase {
    GetCurrentRecordData.Result execute(GetCurrentRecordData.Command command);
}
