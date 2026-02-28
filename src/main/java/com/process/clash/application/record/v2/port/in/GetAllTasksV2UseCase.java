package com.process.clash.application.record.v2.port.in;

import com.process.clash.application.record.v2.data.GetAllTasksV2Data;

public interface GetAllTasksV2UseCase {

    GetAllTasksV2Data.Result execute(GetAllTasksV2Data.Command command);
}
