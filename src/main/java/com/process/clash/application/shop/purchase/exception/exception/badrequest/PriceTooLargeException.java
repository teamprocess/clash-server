package com.process.clash.application.shop.purchase.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.shop.purchase.exception.status.PurchaseStatusCode;

public class PriceTooLargeException extends BadRequestException {
    public PriceTooLargeException() {
        super(PurchaseStatusCode.PRICE_TOO_LARGE);
    }

    public PriceTooLargeException(Throwable cause) {
        super(PurchaseStatusCode.PRICE_TOO_LARGE, cause);
    }
}
