package com.process.clash.application.mainpage.port.in;

import com.process.clash.application.mainpage.data.GetMyRivalActingData;

public interface GetMyRivalActingUseCase {

    GetMyRivalActingData.Result execute(GetMyRivalActingData.Command command);
}
