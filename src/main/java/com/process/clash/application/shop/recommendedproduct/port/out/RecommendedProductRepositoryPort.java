package com.process.clash.application.shop.recommendedproduct.port.out;

import com.process.clash.domain.shop.recommendedproduct.entity.RecommendedProduct;

import java.util.List;
import java.util.Optional;

public interface RecommendedProductRepositoryPort {
    RecommendedProduct save(RecommendedProduct recommendation);
    Optional<RecommendedProduct> findById(Long id);
    boolean existsById(Long id);
    void deleteById(Long id);
    List<RecommendedProduct> findTop10ByIsActiveTrueOrderByDisplayOrder();
}
