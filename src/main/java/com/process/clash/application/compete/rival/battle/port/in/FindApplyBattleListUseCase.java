package com.process.clash.application.compete.rival.battle.port.in;

import com.process.clash.application.compete.rival.battle.data.FindApplyBattleListData;

public interface FindApplyBattleListUseCase {

    FindApplyBattleListData.Result execute(FindApplyBattleListData.Command command);
}