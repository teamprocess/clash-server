package com.process.clash.application.compete.rival.port.in;

import com.process.clash.application.compete.rival.data.AcceptRivalData;

public interface AcceptRivalUseCase {

    void execute(AcceptRivalData.Command command);
}
