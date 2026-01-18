package com.process.clash.application.shop.product.exception.status;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatusCode implements StatusCode {
    PRODUCT_NOT_FOUND("PRODUCT_NOT_FOUND", "상품을 찾을 수 없습니다.", ErrorCategory.NOT_FOUND),
    INVALID_TITLE("INVALID_TITLE", "상품 제목은 필수입니다.", ErrorCategory.BAD_REQUEST),
    INVALID_PRICE("INVALID_PRICE", "가격은 0 이상이어야 합니다.", ErrorCategory.BAD_REQUEST),
    INVALID_DISCOUNT("INVALID_DISCOUNT", "할인율은 0~100 사이여야 합니다.", ErrorCategory.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
