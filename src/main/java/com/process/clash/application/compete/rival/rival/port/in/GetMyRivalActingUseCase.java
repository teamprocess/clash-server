package com.process.clash.application.compete.rival.rival.port.in;

import com.process.clash.application.compete.rival.rival.data.GetMyRivalActingData;

public interface GetMyRivalActingUseCase {

    GetMyRivalActingData.Result execute(GetMyRivalActingData.Command command);
}
