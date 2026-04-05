package com.process.clash.application.shop.product.port.in;

import com.process.clash.application.shop.product.data.UpdateProductData;

public interface UpdateProductUseCase {
    UpdateProductData.Result execute(UpdateProductData.Command command);
}
