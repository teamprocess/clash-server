package com.process.clash.application.major.port.in;

import com.process.clash.application.major.data.MajorTestSubmitData;

public interface MajorTestSubmitUseCase {

    void execute(MajorTestSubmitData.Command command);
}
