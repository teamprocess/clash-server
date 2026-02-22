package com.process.clash.application.compete.rival.battle.exception.exception.conflict;

import com.process.clash.application.common.exception.exception.ConflictException;
import com.process.clash.application.compete.rival.battle.exception.statuscode.BattleStatusCode;

public class AlreadyAppliedBattleException extends ConflictException {
    public AlreadyAppliedBattleException() {
        super(BattleStatusCode.ALREADY_APPLIED_BATTLE);
    }

    public AlreadyAppliedBattleException(Throwable cause) {
        super(BattleStatusCode.ALREADY_APPLIED_BATTLE, cause);
    }
}
