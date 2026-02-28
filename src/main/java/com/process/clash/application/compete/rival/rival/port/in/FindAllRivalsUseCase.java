package com.process.clash.application.compete.rival.rival.port.in;

import com.process.clash.application.compete.rival.rival.data.FindAllRivalsData;

public interface FindAllRivalsUseCase {

    FindAllRivalsData.Result execute(FindAllRivalsData.Command command);
}
