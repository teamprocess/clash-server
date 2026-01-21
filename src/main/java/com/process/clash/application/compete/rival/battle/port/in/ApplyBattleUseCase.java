package com.process.clash.application.compete.rival.battle.port.in;

import com.process.clash.application.compete.rival.battle.data.ApplyBattleData;

public interface ApplyBattleUseCase {

    void execute(ApplyBattleData.Command command);
}
