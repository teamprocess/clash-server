package com.process.clash.application.compete.rival.rival.exception.statuscode;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RivalStatusCode implements StatusCode {

    // 400
    TOO_MUCH_RIVALS("TOO_MUCH_RIVALS", "라이벌이 너무 많습니다.", ErrorCategory.BAD_REQUEST),
    CANCEL_RIVAL_INVALID_STATUS("CANCEL_RIVAL_INVALID_STATUS", "대기 중인 라이벌 신청만 취소할 수 있습니다.", ErrorCategory.BAD_REQUEST),

    // 403
    CANCEL_RIVAL_FORBIDDEN("CANCEL_RIVAL_FORBIDDEN", "라이벌 신청을 취소할 권한이 없습니다.", ErrorCategory.FORBIDDEN),

    // 404
    RIVAL_NOT_FOUND("RIVAL_NOT_FOUND", "라이벌이 존재하지 않습니다..", ErrorCategory.NOT_FOUND),

    // 409
    ALREADY_APPLIED_RIVAL("ALREADY_APPLIED_RIVAL", "이미 신청이 있습니다.", ErrorCategory.CONFLICT),
    ;

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}