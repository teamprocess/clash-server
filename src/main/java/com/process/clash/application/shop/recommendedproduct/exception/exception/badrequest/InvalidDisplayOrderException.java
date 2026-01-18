package com.process.clash.application.shop.recommendedproduct.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.shop.recommendedproduct.exception.status.RecommendedProductStatusCode;

public class InvalidDisplayOrderException extends BadRequestException {
    public InvalidDisplayOrderException() {
        super(RecommendedProductStatusCode.INVALID_DISPLAY_ORDER);
    }

    public InvalidDisplayOrderException(Throwable cause) {
        super(RecommendedProductStatusCode.INVALID_DISPLAY_ORDER, cause);
    }
}
