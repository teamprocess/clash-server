package com.process.clash.application.compete.rival.battle.exception.exception.forbidden;

import com.process.clash.application.common.exception.exception.ForbiddenException;
import com.process.clash.application.compete.rival.battle.exception.statuscode.BattleStatusCode;

public class CancelBattleForbiddenException extends ForbiddenException {
    public CancelBattleForbiddenException() {
        super(BattleStatusCode.CANCEL_BATTLE_FORBIDDEN);
    }
}
