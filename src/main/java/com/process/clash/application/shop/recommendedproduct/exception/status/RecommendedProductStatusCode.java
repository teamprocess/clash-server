package com.process.clash.application.shop.recommendedproduct.exception.status;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecommendedProductStatusCode implements StatusCode {
    RECOMMENDATION_NOT_FOUND("RECOMMENDATION_NOT_FOUND", "추천 상품을 찾을 수 없습니다.", ErrorCategory.NOT_FOUND),
    ;

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
