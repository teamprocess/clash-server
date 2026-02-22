package com.process.clash.application.record.v2.port.in;

import com.process.clash.application.record.v2.data.CreateSubjectTaskV2Data;

public interface CreateSubjectTaskV2UseCase {

    void execute(CreateSubjectTaskV2Data.Command command);
}
