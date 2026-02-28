package com.process.clash.application.compete.rival.rival.port.in;

import com.process.clash.application.compete.rival.rival.data.ModifyRivalData;

public interface CancelRivalUseCase {

    void execute(ModifyRivalData.Command command);
}
