package com.process.clash.application.shop.product.exception.exception.conflict;

import com.process.clash.application.common.exception.exception.ConflictException;
import com.process.clash.application.shop.product.exception.status.ProductStatusCode;

public class ProductInUseException extends ConflictException {
    public ProductInUseException() {
        super(ProductStatusCode.PRODUCT_IN_USE);
    }

    public ProductInUseException(Throwable cause) {
        super(ProductStatusCode.PRODUCT_IN_USE, cause);
    }
}
