package com.process.clash.application.shop.product.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.shop.product.exception.status.ProductStatusCode;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException() {
        super(ProductStatusCode.PRODUCT_NOT_FOUND);
    }

    public ProductNotFoundException(Throwable cause) {
        super(ProductStatusCode.PRODUCT_NOT_FOUND, cause);
    }
}
