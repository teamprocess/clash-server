package com.process.clash.application.profile.port.in;

import com.process.clash.application.profile.data.EquipMyItemData;

public interface EquipMyItemUsecase {
    EquipMyItemData.Result execute(EquipMyItemData.Command command);
}
