package com.process.clash.application.mainpage.port.in.rival;

import com.process.clash.application.compete.rival.data.AddRivalData;

public interface AddRivalUseCase {

    void execute(AddRivalData.Command command);
}
