package com.process.clash.adapter.persistence.shop.recommendedproduct;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendedProductJpaRepository extends JpaRepository<RecommendedProductJpaEntity, Long> {
    List<RecommendedProductJpaEntity> findTop10ByIsActiveTrueOrderByDisplayOrderAsc();
}
