package com.process.clash.application.profile.port.in;

import com.process.clash.application.compete.rival.rival.data.GetMyRivalActingData;
import com.process.clash.application.profile.data.GetUserRivalsData;

public interface GetUserRivalsUsecase {
    GetMyRivalActingData.Result execute(GetUserRivalsData.Command command);
}
