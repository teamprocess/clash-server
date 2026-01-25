package com.process.clash.application.compete.my.exception.statuscode;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MyCompeteStatusCode implements StatusCode {

    // 400
    INVALID_DAY_CATEGORY("INVALID_DAY_CATEGORY", "잘못된 날짜 정보 입니다.", ErrorCategory.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
