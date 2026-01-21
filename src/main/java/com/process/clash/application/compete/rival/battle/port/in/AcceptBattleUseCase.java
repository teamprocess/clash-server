package com.process.clash.application.compete.rival.battle.port.in;

import com.process.clash.application.compete.rival.battle.data.ModifyBattleData;

public interface AcceptBattleUseCase {

    void execute(ModifyBattleData.Command command);
}
