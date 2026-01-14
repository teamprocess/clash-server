package com.process.clash.application.mainpage.port.in.rival;

import com.process.clash.application.mainpage.data.rival.GetAllAbleRivalsData;

public interface GetAllAbleRivalsUseCase {

    GetAllAbleRivalsData.Result execute(GetAllAbleRivalsData.Command command);
}
