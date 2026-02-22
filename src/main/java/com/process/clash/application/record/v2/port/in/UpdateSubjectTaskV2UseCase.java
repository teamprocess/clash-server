package com.process.clash.application.record.v2.port.in;

import com.process.clash.application.record.v2.data.UpdateSubjectTaskV2Data;

public interface UpdateSubjectTaskV2UseCase {

    UpdateSubjectTaskV2Data.Result execute(UpdateSubjectTaskV2Data.Command command);
}
