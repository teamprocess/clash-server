package com.process.clash.adapter.persistence.shop.product;

import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.domain.shop.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository productJpaRepository;
    private final ProductJpaMapper productJpaMapper;

    @Override
    public Product save(Product product) {
        ProductJpaEntity productJpaEntity = productJpaMapper.toJpaEntity(product);
        ProductJpaEntity savedEntity = productJpaRepository.save(productJpaEntity);
        return productJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return productJpaRepository.findById(productId)
                .map(productJpaMapper::toDomain);
    }

    @Override
    public List<Product> findTop10ByOrderByPopularityDesc() {
        return productJpaRepository.findTop10ByOrderByPopularityDesc()
                .stream()
                .map(productJpaMapper::toDomain)
                .toList();
    }
}
