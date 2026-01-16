package com.process.clash.application.shop.recommendedproduct.port.in;

import com.process.clash.application.shop.recommendedproduct.data.UpdateRecommendedProductOrderData;

public interface UpdateRecommendedProductOrderUseCase {
    UpdateRecommendedProductOrderData.Result execute(UpdateRecommendedProductOrderData.Command command);
}
