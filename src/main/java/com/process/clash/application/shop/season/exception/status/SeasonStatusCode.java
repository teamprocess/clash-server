package com.process.clash.application.shop.season.exception.status;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SeasonStatusCode implements StatusCode {
    SEASON_ALREADY_EXISTS("SEASON_ALREADY_EXISTS", "이미 존재하는 시즌명입니다.", ErrorCategory.CONFLICT),
    ;

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
