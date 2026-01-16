package com.process.clash.application.shop.recommendedproduct.service;

import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.shop.recommendedproduct.data.GetRecommendedProductsData;
import com.process.clash.application.shop.recommendedproduct.port.in.GetRecommendedProductsUseCase;
import com.process.clash.application.shop.recommendedproduct.port.out.RecommendedProductRepositoryPort;
import com.process.clash.application.shop.season.port.out.SeasonRepositoryPort;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.recommendedproduct.entity.RecommendedProduct;
import com.process.clash.domain.shop.season.entity.Season;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetRecommendedProductsService implements GetRecommendedProductsUseCase {

    private final RecommendedProductRepositoryPort recommendedProductRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final SeasonRepositoryPort seasonRepositoryPort;

    @Override
    public GetRecommendedProductsData.Result execute() {

        List<RecommendedProduct> recommendations = recommendedProductRepositoryPort.findTop10ByIsActiveTrueOrderByDisplayOrder();

        List<Product> products = new ArrayList<>();
        List<String> seasonTitles = new ArrayList<>();

        for (RecommendedProduct recommendedProduct : recommendations) {
            Product product = productRepositoryPort.findById(recommendedProduct.productId()).orElse(null);
            if (product != null) {
                products.add(product);

                String seasonTitle = null;
                if (product.seasonId() != null) {
                    seasonTitle = seasonRepositoryPort.findById(product.seasonId())
                            .map(Season::title)
                            .orElse(null);
                }
                seasonTitles.add(seasonTitle);
            }
        }

        return GetRecommendedProductsData.Result.from(products, seasonTitles);
    }
}
