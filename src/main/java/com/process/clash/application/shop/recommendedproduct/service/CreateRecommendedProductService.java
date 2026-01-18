package com.process.clash.application.shop.recommendedproduct.service;

import com.process.clash.application.shop.product.exception.exception.notfound.ProductNotFoundException;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.shop.recommendedproduct.data.CreateRecommendedProductData;
import com.process.clash.application.shop.recommendedproduct.port.in.CreateRecommendedProductUseCase;
import com.process.clash.application.shop.recommendedproduct.port.out.RecommendedProductRepositoryPort;
import com.process.clash.application.common.policy.CheckAdminPolicy;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.recommendedproduct.entity.RecommendedProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateRecommendedProductService implements CreateRecommendedProductUseCase {

    private final RecommendedProductRepositoryPort recommendedProductRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    public CreateRecommendedProductData.Result execute(CreateRecommendedProductData.Command command) {
        checkAdminPolicy.check(command.actor());

        Product product = productRepositoryPort.findById(command.productId())
                .orElseThrow(ProductNotFoundException::new);

        RecommendedProduct recommendedProduct = RecommendedProduct.create(
                command.productId(),
                command.displayOrder(),
                command.startDate(),
                command.endDate()
        );

        RecommendedProduct savedRecommendedProduct = recommendedProductRepositoryPort.save(recommendedProduct);

        String seasonName = product.season() != null ? product.season().name() : null;

        return CreateRecommendedProductData.Result.from(savedRecommendedProduct, seasonName);
    }
}
