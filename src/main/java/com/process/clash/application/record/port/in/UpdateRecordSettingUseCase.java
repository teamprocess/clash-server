package com.process.clash.application.record.port.in;

import com.process.clash.application.record.data.UpdateRecordSettingData;

public interface UpdateRecordSettingUseCase {
    UpdateRecordSettingData.Result execute(UpdateRecordSettingData.Command command);
}
