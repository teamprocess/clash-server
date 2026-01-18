package com.process.clash.application.shop.recommendedproduct.port.in;

import com.process.clash.application.shop.recommendedproduct.data.DeleteRecommendedProductData;

public interface DeleteRecommendedProductUseCase {
    void execute(DeleteRecommendedProductData.Command command);
}
