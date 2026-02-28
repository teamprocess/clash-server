package com.process.clash.application.record.v2.port.in;

import com.process.clash.application.record.v2.data.UpdateTaskCompletionV2Data;

public interface UpdateTaskCompletionV2UseCase {

    UpdateTaskCompletionV2Data.Result execute(UpdateTaskCompletionV2Data.Command command);
}
