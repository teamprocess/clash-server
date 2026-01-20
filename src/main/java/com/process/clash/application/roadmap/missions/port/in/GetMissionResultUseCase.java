package com.process.clash.application.roadmap.missions.port.in;

import com.process.clash.application.roadmap.missions.data.GetMissionResultData;

public interface GetMissionResultUseCase {

    GetMissionResultData.Result execute(GetMissionResultData.Command command);
}