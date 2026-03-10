package com.process.clash.application.record.v2.port.in;

import com.process.clash.application.record.v2.data.DeleteTaskV2Data;

public interface DeleteTaskV2UseCase {

    void execute(DeleteTaskV2Data.Command command);
}
