package com.process.clash.application.shop.recommendedproduct.exception.status;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecommendedProductStatusCode implements StatusCode {
    RECOMMENDATION_NOT_FOUND("RECOMMENDATION_NOT_FOUND", "추천 상품을 찾을 수 없습니다.", ErrorCategory.NOT_FOUND),
    INVALID_PRODUCT_ID("INVALID_PRODUCT_ID", "상품 ID는 필수입니다.", ErrorCategory.BAD_REQUEST),
    INVALID_DISPLAY_ORDER("INVALID_DISPLAY_ORDER", "진열 순서는 0 이상이어야 합니다.", ErrorCategory.BAD_REQUEST),
    INVALID_DATE_RANGE("INVALID_DATE_RANGE", "종료일은 시작일보다 이전일 수 없습니다.", ErrorCategory.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
