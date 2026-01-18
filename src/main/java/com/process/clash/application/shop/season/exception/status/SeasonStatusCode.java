package com.process.clash.application.shop.season.exception.status;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SeasonStatusCode implements StatusCode {
    // 409
    SEASON_ALREADY_EXISTS("SEASON_ALREADY_EXISTS", "이미 존재하는 시즌명입니다.", ErrorCategory.CONFLICT),

    // 404
    SEASON_NOT_FOUND("SEASON_NOT_FOUND", "존재하지 않는 시즌입니다.", ErrorCategory.NOT_FOUND),

    // 400
    INVALID_DATE_RANGE("INVALID_DATE_RANGE", "종료일은 시작일보다 이전일 수 없습니다.", ErrorCategory.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
