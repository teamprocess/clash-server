package com.process.clash.application.shop.product.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.shop.product.exception.status.ProductStatusCode;

public class InvalidPriceException extends BadRequestException {
    public InvalidPriceException() {
        super(ProductStatusCode.INVALID_PRICE);
    }

    public InvalidPriceException(Throwable cause) {
        super(ProductStatusCode.INVALID_PRICE, cause);
    }
}
