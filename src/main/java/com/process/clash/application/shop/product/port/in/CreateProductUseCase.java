package com.process.clash.application.shop.product.port.in;

import com.process.clash.application.shop.product.data.CreateProductData;

public interface CreateProductUseCase {
    CreateProductData.Result execute(CreateProductData.Command command);
}
