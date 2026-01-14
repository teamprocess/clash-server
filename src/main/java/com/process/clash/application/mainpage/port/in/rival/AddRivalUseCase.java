package com.process.clash.application.mainpage.port.in.rival;

import com.process.clash.application.mainpage.data.rival.AddRivalData;

public interface AddRivalUseCase {

    void execute(AddRivalData.Command command);
}
