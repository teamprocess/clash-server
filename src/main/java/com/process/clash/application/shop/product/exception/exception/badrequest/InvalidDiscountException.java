package com.process.clash.application.shop.product.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.shop.product.exception.status.ProductStatusCode;

public class InvalidDiscountException extends BadRequestException {
    public InvalidDiscountException() {
        super(ProductStatusCode.INVALID_DISCOUNT);
    }

    public InvalidDiscountException(Throwable cause) {
        super(ProductStatusCode.INVALID_DISCOUNT, cause);
    }
}
