package com.process.clash.application.record.v2.port.in;

import com.process.clash.application.record.v2.data.GetAllSubjectsV2Data;

public interface GetAllSubjectsV2UseCase {

    GetAllSubjectsV2Data.Result execute(GetAllSubjectsV2Data.Command command);
}
