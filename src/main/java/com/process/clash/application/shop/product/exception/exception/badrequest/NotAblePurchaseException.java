package com.process.clash.application.shop.product.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.shop.product.exception.status.ProductStatusCode;

public class NotAblePurchaseException extends BadRequestException {
    public NotAblePurchaseException() {
        super(ProductStatusCode.NOT_ABLE_PURCHASE);
    }
}