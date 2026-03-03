package com.process.clash.application.profile.port.in;

import com.process.clash.application.profile.data.UnequipMyItemData;

public interface UnequipMyItemUsecase {
    UnequipMyItemData.Result execute(UnequipMyItemData.Command command);
}
