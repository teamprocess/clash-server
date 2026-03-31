package com.process.clash.application.shop.product.port.in;

import com.process.clash.application.shop.product.data.DeleteProductData;

public interface DeleteProductUseCase {
    DeleteProductData.Result execute(DeleteProductData.Command command);
}
