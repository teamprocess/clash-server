package com.process.clash.application.compete.rival.battle.port.in;

import com.process.clash.application.compete.rival.battle.data.FindDetailedBattleInfoData;

public interface FindDetailedBattleInfoUseCase {

    FindDetailedBattleInfoData.Result execute(FindDetailedBattleInfoData.Command command);
}
