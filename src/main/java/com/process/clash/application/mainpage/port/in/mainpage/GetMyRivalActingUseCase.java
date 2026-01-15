package com.process.clash.application.mainpage.port.in.mainpage;

import com.process.clash.application.compete.rival.data.GetMyRivalActingData;

public interface GetMyRivalActingUseCase {

    GetMyRivalActingData.Result execute(GetMyRivalActingData.Command command);
}
