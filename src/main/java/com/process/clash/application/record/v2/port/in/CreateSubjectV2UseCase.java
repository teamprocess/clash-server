package com.process.clash.application.record.v2.port.in;

import com.process.clash.application.record.v2.data.CreateSubjectV2Data;

public interface CreateSubjectV2UseCase {

    void execute(CreateSubjectV2Data.Command command);
}
