package com.process.clash.application.record.port.in;

import com.process.clash.application.record.data.GetTodayRecordData;

public interface GetTodayRecordUseCase {
    GetTodayRecordData.Result execute(GetTodayRecordData.Command command);
}
