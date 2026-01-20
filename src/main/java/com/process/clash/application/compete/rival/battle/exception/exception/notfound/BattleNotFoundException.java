package com.process.clash.application.compete.rival.battle.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.compete.rival.battle.exception.statuscode.BattleStatusCode;

public class BattleNotFoundException extends NotFoundException {
    public BattleNotFoundException() {
        super(BattleStatusCode.BATTLE_NOT_FOUND);
    }

    public BattleNotFoundException(Throwable cause) {
        super(BattleStatusCode.BATTLE_NOT_FOUND, cause);
    }
}
