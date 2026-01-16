package com.process.clash.domain.shop.recommendedproduct.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RecommendedProduct(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long productId,
        Integer displayOrder,
        LocalDate startDate,
        LocalDate endDate,
        Boolean isActive
) {
    public RecommendedProduct {
        if (productId == null) {
            throw new IllegalArgumentException("상품 ID는 필수입니다.");
        }
        if (displayOrder == null || displayOrder < 0) {
            throw new IllegalArgumentException("진열 순서는 0 이상이어야 합니다.");
        }
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("종료일은 시작일보다 이전일 수 없습니다.");
        }
    }

    public static RecommendedProduct create(
            Long productId,
            Integer displayOrder,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return new RecommendedProduct(
                null,
                null,
                null,
                productId,
                displayOrder,
                startDate,
                endDate,
                true
        );
    }

    public RecommendedProduct update(
            Integer displayOrder,
            LocalDate startDate,
            LocalDate endDate,
            Boolean isActive
    ) {
        return new RecommendedProduct(
                this.id,
                this.createdAt,
                LocalDateTime.now(),
                this.productId,
                displayOrder,
                startDate,
                endDate,
                isActive
        );
    }

    public RecommendedProduct updateOrder(Integer displayOrder) {
        return new RecommendedProduct(
                this.id,
                this.createdAt,
                LocalDateTime.now(),
                this.productId,
                displayOrder,
                this.startDate,
                this.endDate,
                this.isActive
        );
    }
}
