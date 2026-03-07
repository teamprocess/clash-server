package com.process.clash.application.compete.rival.battle.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.compete.rival.battle.exception.statuscode.BattleStatusCode;

public class CancelBattleInvalidStatusException extends BadRequestException {
    public CancelBattleInvalidStatusException() {
        super(BattleStatusCode.CANCEL_BATTLE_INVALID_STATUS);
    }
}
