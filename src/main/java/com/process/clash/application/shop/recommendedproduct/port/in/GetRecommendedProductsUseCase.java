package com.process.clash.application.shop.recommendedproduct.port.in;

import com.process.clash.application.shop.recommendedproduct.data.GetRecommendedProductsData;

public interface GetRecommendedProductsUseCase {
    GetRecommendedProductsData.Result execute();
}
