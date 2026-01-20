package com.process.clash.application.record.port.in;

import com.process.clash.application.record.data.CreateTaskData;

public interface CreateTaskUseCase {
    void execute(CreateTaskData.Command command);
}
