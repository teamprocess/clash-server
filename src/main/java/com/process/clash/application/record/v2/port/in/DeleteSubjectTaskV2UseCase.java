package com.process.clash.application.record.v2.port.in;

import com.process.clash.application.record.v2.data.DeleteSubjectTaskV2Data;

public interface DeleteSubjectTaskV2UseCase {

    void execute(DeleteSubjectTaskV2Data.Command command);
}
