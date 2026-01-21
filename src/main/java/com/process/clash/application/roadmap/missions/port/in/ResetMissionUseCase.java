package com.process.clash.application.roadmap.missions.port.in;

import com.process.clash.application.roadmap.missions.data.ResetMissionData;

public interface ResetMissionUseCase {

    void execute(ResetMissionData.Command command);
}