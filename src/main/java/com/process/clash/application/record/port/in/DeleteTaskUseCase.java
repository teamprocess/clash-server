package com.process.clash.application.record.port.in;

import com.process.clash.application.record.dto.DeleteTaskData;

public interface DeleteTaskUseCase {
    void execute(DeleteTaskData.Command command);
}
