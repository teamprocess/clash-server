package com.process.clash.adapter.persistence.shop.recommendedproduct;

import com.process.clash.application.shop.recommendedproduct.port.out.RecommendedProductRepositoryPort;
import com.process.clash.domain.shop.recommendedproduct.entity.RecommendedProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RecommendedProductPersistenceAdapter implements RecommendedProductRepositoryPort {

    private final RecommendedProductJpaRepository recommendedProductJpaRepository;
    private final RecommendedProductJpaMapper recommendedProductJpaMapper;

    @Override
    public RecommendedProduct save(RecommendedProduct recommendedProduct) {
        RecommendedProductJpaEntity entity = recommendedProductJpaMapper.toJpaEntity(recommendedProduct);
        RecommendedProductJpaEntity savedEntity = recommendedProductJpaRepository.save(entity);
        return recommendedProductJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<RecommendedProduct> findById(Long id) {
        return recommendedProductJpaRepository.findById(id)
                .map(recommendedProductJpaMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        recommendedProductJpaRepository.deleteById(id);
    }

    @Override
    public List<RecommendedProduct> findTop10ByIsActiveTrueOrderByDisplayOrder() {
        return recommendedProductJpaRepository.findTop10ByIsActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(recommendedProductJpaMapper::toDomain)
                .toList();
    }
}
