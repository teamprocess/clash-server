package com.process.clash.application.compete.rival.rival.port.in;

import com.process.clash.application.compete.rival.rival.data.CompareWithRivalsData;

public interface CompareWithRivalsUseCase {

    CompareWithRivalsData.Result execute(CompareWithRivalsData.Command command);
}
