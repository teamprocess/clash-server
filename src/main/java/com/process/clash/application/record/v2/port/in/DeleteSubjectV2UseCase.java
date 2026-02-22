package com.process.clash.application.record.v2.port.in;

import com.process.clash.application.record.v2.data.DeleteSubjectV2Data;

public interface DeleteSubjectV2UseCase {

    void execute(DeleteSubjectV2Data.Command command);
}
