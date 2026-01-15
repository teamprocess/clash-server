package com.process.clash.application.mainpage.port.in.rival;

import com.process.clash.application.compete.rival.data.GetAllAbleRivalsData;

public interface GetAllAbleRivalsUseCase {

    GetAllAbleRivalsData.Result execute(GetAllAbleRivalsData.Command command);
}
