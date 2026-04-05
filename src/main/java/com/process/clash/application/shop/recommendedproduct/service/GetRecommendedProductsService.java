package com.process.clash.application.shop.recommendedproduct.service;

import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.shop.product.service.ProductVoConverter;
import com.process.clash.application.shop.product.vo.ProductVo;
import com.process.clash.application.shop.recommendedproduct.data.GetRecommendedProductsData;
import com.process.clash.application.shop.recommendedproduct.port.in.GetRecommendedProductsUseCase;
import com.process.clash.application.shop.recommendedproduct.port.out.RecommendedProductRepositoryPort;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.recommendedproduct.entity.RecommendedProduct;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetRecommendedProductsService implements GetRecommendedProductsUseCase {

    private final RecommendedProductRepositoryPort recommendedProductRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final ProductVoConverter productVoConverter;

    @Override
    public GetRecommendedProductsData.Result execute(GetRecommendedProductsData.Command command) {
        List<RecommendedProduct> recommendations = recommendedProductRepositoryPort
                .findTop10ActiveByDateOrderByDisplayOrder(LocalDate.now());

        List<Long> productIds = recommendations.stream()
                .map(RecommendedProduct::productId)
                .toList();

        List<Product> products = productRepositoryPort.findAllByIdIn(productIds);
        Map<Long, Product> productById = new LinkedHashMap<>();
        for (Product product : products) {
            productById.put(product.id(), product);
        }

        List<Product> orderedProducts = productIds.stream()
                .map(productById::get)
                .filter(java.util.Objects::nonNull)
                .toList();

        List<ProductVo> productVos = productVoConverter.toProductVos(orderedProducts, command.actor());

        return new GetRecommendedProductsData.Result(productVos);
    }
}
