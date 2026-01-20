package com.process.clash.application.compete.rival.rival.port.in;

import com.process.clash.application.compete.rival.rival.data.ApplyRivalData;

public interface ApplyRivalUseCase {

    void execute(ApplyRivalData.Command command);
}
