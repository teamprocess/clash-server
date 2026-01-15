package com.process.clash.application.compete.rival.port.in;

import com.process.clash.application.compete.rival.data.AddRivalData;

public interface AddRivalUseCase {

    void execute(AddRivalData.Command command);
}
