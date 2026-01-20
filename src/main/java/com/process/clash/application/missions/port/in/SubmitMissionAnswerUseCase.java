package com.process.clash.application.missions.port.in;

import com.process.clash.application.missions.data.SubmitMissionAnswerData;

public interface SubmitMissionAnswerUseCase {

    SubmitMissionAnswerData.Result execute(SubmitMissionAnswerData.Command command);
}