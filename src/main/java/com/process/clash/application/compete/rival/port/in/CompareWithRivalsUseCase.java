package com.process.clash.application.compete.rival.port.in;

import com.process.clash.application.compete.rival.data.CompareWithRivalsData;

public interface CompareWithRivalsUseCase {

    CompareWithRivalsData.Result execute(CompareWithRivalsData.Command command);
}
