package com.process.clash.application.record.port.in;

import com.process.clash.application.record.data.UpdateTaskData;

public interface UpdateTaskUseCase {
    UpdateTaskData.Result execute(UpdateTaskData.Command command);
}
