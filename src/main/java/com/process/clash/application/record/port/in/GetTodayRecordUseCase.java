package com.process.clash.application.record.port.in;

import com.process.clash.application.record.dto.GetTodayRecordData;

public interface GetTodayRecordUseCase {
    GetTodayRecordData.Result execute(GetTodayRecordData.Commmand commmand);
}
