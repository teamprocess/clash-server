package com.process.clash.application.shop.purchase.port.in;

import com.process.clash.application.shop.purchase.data.CreatePurchaseData;

public interface CreatePurchaseUseCase {
    CreatePurchaseData.Result execute(CreatePurchaseData.Command command);
}
