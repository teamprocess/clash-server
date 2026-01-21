package com.process.clash.application.compete.rival.rival.port.in;

import com.process.clash.application.compete.rival.rival.data.GetAllAbleRivalsData;

public interface GetAllAbleRivalsUseCase {

    GetAllAbleRivalsData.Result execute(GetAllAbleRivalsData.Command command);
}
