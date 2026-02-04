package com.process.clash.application.shop.purchase.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.shop.purchase.exception.status.PurchaseStatusCode;

public class InsufficientGoodsException extends BadRequestException {
    public InsufficientGoodsException() {
        super(PurchaseStatusCode.INSUFFICIENT_GOODS);
    }

    public InsufficientGoodsException(Throwable cause) {
        super(PurchaseStatusCode.INSUFFICIENT_GOODS, cause);
    }
}
