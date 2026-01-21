package com.process.clash.application.compete.rival.battle.port.in;

import com.process.clash.application.compete.rival.battle.data.FindAllBattleInfoData;

public interface FindAllBattleInfoUseCase {

    FindAllBattleInfoData.Result execute(FindAllBattleInfoData.Command command);
}
