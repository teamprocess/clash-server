package com.process.clash.application.record.port.in;

import com.process.clash.application.record.dto.UpdateRecordSettingData;

public interface UpdateRecordSettingUseCase {
    UpdateRecordSettingData.Result execute(UpdateRecordSettingData.Command command);
}
