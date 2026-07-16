package com.process.clash.application.helpcontent.port.in;

import com.process.clash.application.helpcontent.data.UpdateHelpContentData;

public interface UpdateHelpContentUseCase {

    UpdateHelpContentData.Result execute(UpdateHelpContentData.Command command);
}
