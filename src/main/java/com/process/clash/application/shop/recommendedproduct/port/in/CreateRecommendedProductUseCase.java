package com.process.clash.application.shop.recommendedproduct.port.in;

import com.process.clash.application.shop.recommendedproduct.data.CreateRecommendedProductData;

public interface CreateRecommendedProductUseCase {
    CreateRecommendedProductData.Result execute(CreateRecommendedProductData.Command command);
}
