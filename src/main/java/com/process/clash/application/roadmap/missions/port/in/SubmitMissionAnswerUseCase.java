package com.process.clash.application.roadmap.missions.port.in;

import com.process.clash.application.roadmap.missions.data.SubmitMissionAnswerData;

public interface SubmitMissionAnswerUseCase {

    SubmitMissionAnswerData.Result execute(SubmitMissionAnswerData.Command command);
}