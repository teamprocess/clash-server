package com.process.clash.application.shop.recommendedproduct.port.in;

import com.process.clash.application.shop.recommendedproduct.data.UpdateRecommendedProductData;

public interface UpdateRecommendedProductUseCase {
    UpdateRecommendedProductData.Result execute(UpdateRecommendedProductData.Command command);
}
