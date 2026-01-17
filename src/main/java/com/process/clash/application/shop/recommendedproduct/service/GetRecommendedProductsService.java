package com.process.clash.application.shop.recommendedproduct.service;

import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.shop.recommendedproduct.data.GetRecommendedProductsData;
import com.process.clash.application.shop.recommendedproduct.port.in.GetRecommendedProductsUseCase;
import com.process.clash.application.shop.recommendedproduct.port.out.RecommendedProductRepositoryPort;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.recommendedproduct.entity.RecommendedProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetRecommendedProductsService implements GetRecommendedProductsUseCase {

    private final RecommendedProductRepositoryPort recommendedProductRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public GetRecommendedProductsData.Result execute() {
        List<RecommendedProduct> recommendations = recommendedProductRepositoryPort.findTop10ByIsActiveTrueOrderByDisplayOrder();

        List<Long> productIds = recommendations.stream()
                .map(RecommendedProduct::productId)
                .toList();

        List<Product> products = productRepositoryPort.findAllByIdIn(productIds);

        return GetRecommendedProductsData.Result.from(products);
    }
}
