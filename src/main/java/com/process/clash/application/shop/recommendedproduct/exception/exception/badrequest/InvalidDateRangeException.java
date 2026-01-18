package com.process.clash.application.shop.recommendedproduct.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.shop.recommendedproduct.exception.status.RecommendedProductStatusCode;

public class InvalidDateRangeException extends BadRequestException {
    public InvalidDateRangeException() {
        super(RecommendedProductStatusCode.INVALID_DATE_RANGE);
    }

    public InvalidDateRangeException(Throwable cause) {
        super(RecommendedProductStatusCode.INVALID_DATE_RANGE, cause);
    }
}
