package com.process.clash.application.compete.rival.battle.port.in;

import com.process.clash.application.compete.rival.battle.data.AnalyzeBattleInfoData;

public interface AnalyzeBattleInfoUseCase {

    AnalyzeBattleInfoData.Result execute(AnalyzeBattleInfoData.Command command);
}
