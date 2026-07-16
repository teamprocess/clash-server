package com.process.clash.application.helpcontent.port.in;

import com.process.clash.application.helpcontent.data.GetHelpContentData;

public interface GetHelpContentUseCase {

    GetHelpContentData.Result execute(String key);
}
