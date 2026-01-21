package com.process.clash.application.compete.rival.battle.port.in;

import com.process.clash.application.compete.rival.battle.data.ModifyBattleData;

public interface RejectBattleUseCase {

    void execute(ModifyBattleData.Command command);
}
