package com.process.clash.domain.shop.recommendedproduct.entity;

import com.process.clash.application.shop.recommendedproduct.exception.exception.badrequest.InvalidDateRangeException;
import com.process.clash.application.shop.recommendedproduct.exception.exception.badrequest.InvalidDisplayOrderException;
import com.process.clash.application.shop.recommendedproduct.exception.exception.badrequest.InvalidProductIdException;
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
            throw new InvalidProductIdException();
        }
        if (displayOrder == null || displayOrder < 0) {
            throw new InvalidDisplayOrderException();
        }
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new InvalidDateRangeException();
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
