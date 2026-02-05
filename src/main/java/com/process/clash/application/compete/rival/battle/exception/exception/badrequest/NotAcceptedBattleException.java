package com.process.clash.application.compete.rival.battle.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.compete.rival.battle.exception.statuscode.BattleStatusCode;

public class NotAcceptedBattleException extends BadRequestException {
    public NotAcceptedBattleException() {
        super(BattleStatusCode.NOT_ACCEPTED_BATTLE);
    }

    public NotAcceptedBattleException(Throwable cause) {
        super(BattleStatusCode.NOT_ACCEPTED_BATTLE, cause);
    }
}
