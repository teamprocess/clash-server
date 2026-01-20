package com.process.clash.application.missions.port.in;

import com.process.clash.application.missions.data.ResetMissionData;

public interface ResetMissionUseCase {

    void execute(ResetMissionData.Command command);
}