package com.process.clash.application.shop.purchase.exception.status;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PurchaseStatusCode implements StatusCode {
    ALREADY_OWNED_PRODUCT("ALREADY_OWNED_PRODUCT", "이미 보유한 상품입니다.", ErrorCategory.CONFLICT),
    INSUFFICIENT_GOODS("INSUFFICIENT_GOODS", "재화가 부족합니다.", ErrorCategory.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
