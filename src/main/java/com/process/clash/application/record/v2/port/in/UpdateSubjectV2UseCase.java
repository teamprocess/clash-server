package com.process.clash.application.record.v2.port.in;

import com.process.clash.application.record.v2.data.UpdateSubjectV2Data;

public interface UpdateSubjectV2UseCase {

    UpdateSubjectV2Data.Result execute(UpdateSubjectV2Data.Command command);
}
