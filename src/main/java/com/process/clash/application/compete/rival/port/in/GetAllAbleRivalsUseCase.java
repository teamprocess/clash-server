package com.process.clash.application.compete.rival.port.in;

import com.process.clash.application.compete.rival.data.GetAllAbleRivalsData;

public interface GetAllAbleRivalsUseCase {

    GetAllAbleRivalsData.Result execute(GetAllAbleRivalsData.Command command);
}
