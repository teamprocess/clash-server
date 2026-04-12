package com.process.clash.application.profile.exception.statuscode;

import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProfileStatusCode implements StatusCode {

    INVALID_PERIOD_CATEGORY("INVALID_PERIOD_CATEGORY", "잘못된 PeriodCategory입니다.", HttpStatus.BAD_REQUEST),
    ITEM_NOT_OWNED("ITEM_NOT_OWNED", "보유하지 않은 아이템입니다.", HttpStatus.FORBIDDEN),
    INVALID_EQUIPPABLE_ITEM_CATEGORY("INVALID_EQUIPPABLE_ITEM_CATEGORY", "장착 가능한 아이템 카테고리가 아닙니다.", HttpStatus.BAD_REQUEST),
    PROFILE_PRIVATED("PROFILE_PRIVATED", "비공개 프로필입니다.", HttpStatus.FORBIDDEN);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
