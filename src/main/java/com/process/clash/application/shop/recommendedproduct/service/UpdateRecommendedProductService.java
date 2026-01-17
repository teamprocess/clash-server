package com.process.clash.application.shop.recommendedproduct.service;

import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.shop.recommendedproduct.data.UpdateRecommendedProductData;
import com.process.clash.application.shop.recommendedproduct.exception.exception.notfound.RecommendedProductNotFoundException;
import com.process.clash.application.shop.recommendedproduct.port.in.UpdateRecommendedProductUseCase;
import com.process.clash.application.shop.recommendedproduct.port.out.RecommendedProductRepositoryPort;
import com.process.clash.domain.common.policy.CheckAdminPolicy;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.recommendedproduct.entity.RecommendedProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateRecommendedProductService implements UpdateRecommendedProductUseCase {

    private final RecommendedProductRepositoryPort recommendedProductRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    public UpdateRecommendedProductData.Result execute(UpdateRecommendedProductData.Command command) {
        checkAdminPolicy.check(command.actor());

        RecommendedProduct recommendedProduct = recommendedProductRepositoryPort.findById(command.recommendedProductId())
                .orElseThrow(RecommendedProductNotFoundException::new);

        RecommendedProduct updatedRecommendedProduct = recommendedProduct.update(
                command.displayOrder(),
                command.startDate(),
                command.endDate(),
                command.isActive()
        );

        RecommendedProduct savedRecommendedProduct = recommendedProductRepositoryPort.save(updatedRecommendedProduct);

        Product product = productRepositoryPort.findById(savedRecommendedProduct.productId()).orElse(null);
        String seasonName = product != null && product.season() != null ? product.season().name() : null;

        return UpdateRecommendedProductData.Result.from(savedRecommendedProduct, seasonName);
    }
}
