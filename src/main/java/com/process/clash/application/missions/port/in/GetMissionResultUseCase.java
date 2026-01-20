package com.process.clash.application.missions.port.in;

import com.process.clash.application.missions.data.GetMissionResultData;

public interface GetMissionResultUseCase {

    GetMissionResultData.Result execute(GetMissionResultData.Command command);
}