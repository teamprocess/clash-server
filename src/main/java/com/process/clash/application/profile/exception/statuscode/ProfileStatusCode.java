package com.process.clash.application.profile.exception.statuscode;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProfileStatusCode implements StatusCode {

    INVALID_PERIOD_CATEGORY("INVALID_PERIOD_CATEGORY", "잘못된 PeriodCategory입니다.", ErrorCategory.BAD_REQUEST),
    ITEM_NOT_OWNED("ITEM_NOT_OWNED", "보유하지 않은 아이템입니다.", ErrorCategory.FORBIDDEN),
    INVALID_EQUIPPABLE_ITEM_CATEGORY("INVALID_EQUIPPABLE_ITEM_CATEGORY", "장착 가능한 아이템 카테고리가 아닙니다.", ErrorCategory.BAD_REQUEST);

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
