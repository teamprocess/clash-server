package com.process.clash.application.shop.recommendedproduct.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.shop.recommendedproduct.exception.status.RecommendedProductStatusCode;

public class RecommendedProductNotFoundException extends NotFoundException {
    public RecommendedProductNotFoundException() {
        super(RecommendedProductStatusCode.RECOMMENDATION_NOT_FOUND);
    }

    public RecommendedProductNotFoundException(Throwable cause) {
        super(RecommendedProductStatusCode.RECOMMENDATION_NOT_FOUND, cause);
    }
}
