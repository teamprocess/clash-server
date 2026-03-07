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
    CANCEL_BATTLE_INVALID_STATUS("CANCEL_BATTLE_INVALID_STATUS", "대기 중인 배틀만 취소할 수 있습니다.", ErrorCategory.BAD_REQUEST),

    // 403
    CANCEL_BATTLE_FORBIDDEN("CANCEL_BATTLE_FORBIDDEN", "배틀 신청자만 취소할 수 있습니다.", ErrorCategory.FORBIDDEN),
    REJECT_BATTLE_FORBIDDEN("REJECT_BATTLE_FORBIDDEN", "배틀을 거절할 권한이 없습니다.", ErrorCategory.FORBIDDEN),

    // 404
    BATTLE_NOT_FOUND("BATTLE_NOT_FOUND", "배틀이 존재하지 않습니다.", ErrorCategory.NOT_FOUND),

    // 409
    ALREADY_IN_BATTLE("ALREADY_IN_BATTLE", "이미 배틀이 존재합니다.", ErrorCategory.CONFLICT),
    ALREADY_APPLIED_BATTLE("ALREADY_APPLIED_BATTLE", "이미 신청이 있습니다.", ErrorCategory.CONFLICT),
    ;

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
