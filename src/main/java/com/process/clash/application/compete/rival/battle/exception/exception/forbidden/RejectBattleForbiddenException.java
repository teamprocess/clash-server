package com.process.clash.application.compete.rival.battle.exception.exception.forbidden;

import com.process.clash.application.common.exception.exception.ForbiddenException;
import com.process.clash.application.compete.rival.battle.exception.statuscode.BattleStatusCode;

public class RejectBattleForbiddenException extends ForbiddenException {
    public RejectBattleForbiddenException() {
        super(BattleStatusCode.REJECT_BATTLE_FORBIDDEN);
    }
}
