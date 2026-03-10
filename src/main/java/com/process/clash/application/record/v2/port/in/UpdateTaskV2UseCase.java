package com.process.clash.application.record.v2.port.in;

import com.process.clash.application.record.v2.data.UpdateTaskV2Data;

public interface UpdateTaskV2UseCase {

    UpdateTaskV2Data.Result execute(UpdateTaskV2Data.Command command);
}
