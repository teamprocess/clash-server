package com.process.clash.application.shop.product.port.in;

import com.process.clash.application.shop.product.data.GetAllProductsData;

public interface GetAllProductsUseCase {
    GetAllProductsData.Result execute(GetAllProductsData.Command command);
}
