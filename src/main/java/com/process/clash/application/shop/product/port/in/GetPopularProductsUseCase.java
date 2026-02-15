package com.process.clash.application.shop.product.port.in;

import com.process.clash.application.shop.product.data.GetPopularProductsData;

public interface GetPopularProductsUseCase {
    GetPopularProductsData.Result execute(GetPopularProductsData.Command command);
}
