package com.process.clash.application.helpcontent.port.in;

import com.process.clash.application.helpcontent.data.CreateHelpContentData;

public interface CreateHelpContentUseCase {

    CreateHelpContentData.Result execute(CreateHelpContentData.Command command);
}
