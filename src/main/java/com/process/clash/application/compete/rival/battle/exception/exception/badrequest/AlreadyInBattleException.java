package com.process.clash.application.compete.rival.battle.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.ConflictException;
import com.process.clash.application.compete.rival.battle.exception.statuscode.BattleStatusCode;

public class AlreadyInBattleException extends ConflictException {
    public AlreadyInBattleException() {
        super(BattleStatusCode.ALREADY_IN_BATTLE);
    }

    public AlreadyInBattleException(Throwable cause) {
        super(BattleStatusCode.ALREADY_IN_BATTLE, cause);
    }
}
