package com.process.clash.application.shop.purchase.exception.exception.conflict;

import com.process.clash.application.common.exception.exception.ConflictException;
import com.process.clash.application.shop.purchase.exception.status.PurchaseStatusCode;

public class AlreadyOwnedProductException extends ConflictException {
    public AlreadyOwnedProductException() {
        super(PurchaseStatusCode.ALREADY_OWNED_PRODUCT);
    }

    public AlreadyOwnedProductException(Throwable cause) {
        super(PurchaseStatusCode.ALREADY_OWNED_PRODUCT, cause);
    }
}
