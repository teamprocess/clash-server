package com.process.clash.application.compete.rival.battle.exception.statuscode;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BattleStatusCode implements StatusCode {

    // 400
    NOT_ACCEPTED_BATTLE("NOT_ACCEPTED_BATTLE", "배틀이 승인되지 않았습니다.", ErrorCategory.BAD_REQUEST),

    // 404
    BATTLE_NOT_FOUND("BATTLE_NOT_FOUND", "배틀이 존재하지 않습니다.", ErrorCategory.NOT_FOUND),

    // 409
    ALREADY_IN_BATTLE("ALREADY_IN_BATTLE", "이미 배틀이 존재합니다.", ErrorCategory.CONFLICT),
    ;

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
