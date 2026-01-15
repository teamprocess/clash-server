package com.process.clash.application.compete.rival.port.in;

import com.process.clash.application.compete.rival.data.GetMyRivalActingData;

public interface GetMyRivalActingUseCase {

    GetMyRivalActingData.Result execute(GetMyRivalActingData.Command command);
}
