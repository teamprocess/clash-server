package com.process.clash.application.shop.purchase.port.out;

import com.process.clash.domain.shop.purchase.entity.Purchase;

public interface PurchaseRepositoryPort {

    Purchase save(Purchase purchase);
}
