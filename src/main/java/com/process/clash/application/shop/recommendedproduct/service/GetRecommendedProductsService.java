package com.process.clash.application.shop.recommendedproduct.service;

import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.shop.recommendedproduct.data.GetRecommendedProductsData;
import com.process.clash.application.shop.recommendedproduct.port.in.GetRecommendedProductsUseCase;
import com.process.clash.application.shop.recommendedproduct.port.out.RecommendedProductRepositoryPort;
import com.process.clash.application.user.useritem.port.out.UserItemRepositoryPort;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.recommendedproduct.entity.RecommendedProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetRecommendedProductsService implements GetRecommendedProductsUseCase {

    private final RecommendedProductRepositoryPort recommendedProductRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final UserItemRepositoryPort userItemRepositoryPort;

    @Override
    public GetRecommendedProductsData.Result execute(GetRecommendedProductsData.Command command) {
        List<RecommendedProduct> recommendations = recommendedProductRepositoryPort.findTop10ByIsActiveTrueOrderByDisplayOrder();

        List<Long> productIds = recommendations.stream()
                .map(RecommendedProduct::productId)
                .toList();

        List<Product> products = productRepositoryPort.findAllByIdIn(productIds);
        Set<Long> ownedProductIds = userItemRepositoryPort.findOwnedProductIdsByUserIdAndProductIds(
                command.actor().id(),
                new HashSet<>(productIds)
        );

        return GetRecommendedProductsData.Result.from(products, ownedProductIds);
    }
}
