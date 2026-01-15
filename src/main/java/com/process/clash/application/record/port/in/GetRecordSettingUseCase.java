package com.process.clash.application.record.port.in;

import com.process.clash.application.record.dto.GetRecordSettingData;

public interface GetRecordSettingUseCase {
    GetRecordSettingData.Result execute(GetRecordSettingData.Command command);
}
