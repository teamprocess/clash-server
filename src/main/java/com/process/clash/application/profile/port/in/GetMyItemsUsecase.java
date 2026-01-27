package com.process.clash.application.profile.port.in;

import com.process.clash.application.profile.data.GetMyItemsData;

public interface GetMyItemsUsecase {
    GetMyItemsData.Result execute(GetMyItemsData.Command command);
}
