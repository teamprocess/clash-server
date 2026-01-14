package com.process.clash.application.record.port.in;

import com.process.clash.application.record.dto.GetAllTasksData;

public interface GetAllTasksUseCase {
    GetAllTasksData.Result execute(GetAllTasksData.Command command);
}
