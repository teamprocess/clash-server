package com.process.clash.application.major.port.in;

import com.process.clash.application.major.data.MajorSubmitData;

public interface MajorTestSubmitUseCase {

    void execute(MajorSubmitData.Command command);
}
