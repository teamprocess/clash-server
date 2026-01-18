package com.process.clash.application.compete.rival.exception.statuscode;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RivalStatusCode implements StatusCode {

    // 400
    TOO_MUCH_RIVALS("TOO_MUCH_RIVALS", "라이벌이 너무 많습니다.", ErrorCategory.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}