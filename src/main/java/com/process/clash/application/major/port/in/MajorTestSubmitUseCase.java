package com.process.clash.application.major.port.in;

import com.process.clash.application.major.data.MajorTestSubmitData;
import com.process.clash.domain.common.enums.Major;

public interface MajorTestSubmitUseCase {

    Void execute(MajorTestSubmitData.Command command);
}
