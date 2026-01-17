package com.process.clash.adapter.persistence.shop.recommendedproduct;

import com.process.clash.adapter.persistence.shop.product.ProductJpaEntity;
import com.process.clash.adapter.persistence.shop.product.ProductJpaRepository;
import com.process.clash.domain.shop.recommendedproduct.entity.RecommendedProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecommendedProductJpaMapper {

    private final ProductJpaRepository productJpaRepository;

    public RecommendedProductJpaEntity toJpaEntity(RecommendedProduct recommendedProduct) {
        ProductJpaEntity product = recommendedProduct.productId() != null
                ? productJpaRepository.getReferenceById(recommendedProduct.productId())
                : null;

        return new RecommendedProductJpaEntity(
                recommendedProduct.id(),
                recommendedProduct.createdAt(),
                recommendedProduct.updatedAt(),
                product,
                recommendedProduct.displayOrder(),
                recommendedProduct.startDate(),
                recommendedProduct.endDate(),
                recommendedProduct.isActive()
        );
    }

    public RecommendedProduct toDomain(RecommendedProductJpaEntity entity) {
        return new RecommendedProduct(
                entity.getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getProduct() != null ? entity.getProduct().getId() : null,
                entity.getDisplayOrder(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getIsActive()
        );
    }
}
