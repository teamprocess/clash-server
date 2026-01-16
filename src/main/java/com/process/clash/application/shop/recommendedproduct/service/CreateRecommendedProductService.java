package com.process.clash.application.shop.recommendedproduct.service;

import com.process.clash.application.shop.product.exception.exception.notfound.ProductNotFoundException;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.shop.recommendedproduct.data.CreateRecommendedProductData;
import com.process.clash.application.shop.recommendedproduct.port.in.CreateRecommendedProductUseCase;
import com.process.clash.application.shop.recommendedproduct.port.out.RecommendedProductRepositoryPort;
import com.process.clash.application.shop.season.port.out.SeasonRepositoryPort;
import com.process.clash.domain.common.policy.CheckAdminPolicy;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.recommendedproduct.entity.RecommendedProduct;
import com.process.clash.domain.shop.season.entity.Season;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateRecommendedProductService implements CreateRecommendedProductUseCase {

    private final RecommendedProductRepositoryPort recommendedProductRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final SeasonRepositoryPort seasonRepositoryPort;
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

        String seasonTitle = null;
        if (product.seasonId() != null) {
            seasonTitle = seasonRepositoryPort.findById(product.seasonId())
                    .map(Season::title)
                    .orElse(null);
        }

        return CreateRecommendedProductData.Result.from(savedRecommendedProduct, seasonTitle);
    }
}
