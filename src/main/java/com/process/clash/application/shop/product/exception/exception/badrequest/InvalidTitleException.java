package com.process.clash.application.shop.product.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.shop.product.exception.status.ProductStatusCode;

public class InvalidTitleException extends BadRequestException {
    public InvalidTitleException() {
        super(ProductStatusCode.INVALID_TITLE);
    }

    public InvalidTitleException(Throwable cause) {
        super(ProductStatusCode.INVALID_TITLE, cause);
    }
}
