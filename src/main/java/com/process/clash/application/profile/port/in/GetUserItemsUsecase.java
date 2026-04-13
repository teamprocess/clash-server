package com.process.clash.application.profile.port.in;

import com.process.clash.application.profile.data.GetMyItemsData;
import com.process.clash.application.profile.data.GetUserItemsData;

public interface GetUserItemsUsecase {
    GetMyItemsData.Result execute(GetUserItemsData.Command command);
}
